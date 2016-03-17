/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.joinrules.IdToInstanceJoinRule;
import org.comicwiki.joinrules.JoinRule;
import org.comicwiki.joinrules.LookupJoinRule;
import org.comicwiki.joinrules.NoOpJoinRule;

public abstract class BaseTable<ROW extends TableRow<?>> {

	private static final Logger LOG = Logger.getLogger("BaseTable");

	private static void assertValidJdbcScheme(String jdbcUrl) {
		if (!SparkUtils.isValidScheme(jdbcUrl)) {
			throw new IllegalArgumentException("invalid jdbc scheme: "
					+ jdbcUrl);
		}
	}

	private static BaseTable<? extends TableRow<?>> getJoinTable(
			BaseTable<? extends TableRow<?>>[] tables, Class<?> clazz) {
		for (BaseTable<? extends TableRow<?>> baseTable : tables) {
			if (baseTable.getClass().isAssignableFrom(clazz)) {
				return baseTable;
			}
		}
		return null;
	}

	// public Map<Integer, ROW> rowCache = new Int2ObjectOpenHashMap<ROW>();

	public TIntObjectHashMap<ROW> rowCache = new TIntObjectHashMap<ROW>();

	protected final String datasourceName;

	protected final SQLContext sqlContext;

	private final TableFormat tableFormat;

	public BaseTable(SQLContext sqlContext, String datasourceName) {
		this(sqlContext, datasourceName, TableFormat.RDB);
	}

	public void clear() {
		rowCache.clear();
		rowCache = null;
	}
	
	public BaseTable(SQLContext sqlContext, String datasourceName,
			TableFormat tableFormat) {
		this.sqlContext = sqlContext;
		this.datasourceName = datasourceName;
		this.tableFormat = tableFormat;
	}

	protected final void add(ROW row) {
		rowCache.put(row.id, row);
	}

	protected ArrayList<ROW> cacheToArray() {
		ArrayList<ROW> ar = new ArrayList<>();
		Collection<ROW> rightRows = getCache().valueCollection();
		for (ROW row : rightRows) {
			ar.add(row);
		}
		return ar;
	}

	private DataFrameReader createReader(String jdbcUrl, String tableName) {
		return sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName);
	}

	public final void extract() throws IOException {
		if (sqlContext == null) {
			return;// noop
		}
		DataFrame frame = null;
		if (TableFormat.RDB.equals(tableFormat)) {
			frame = sqlContext.read().load(datasourceName);
		} else if (TableFormat.JSON.equals(tableFormat)) {
			frame = sqlContext.read().json(datasourceName);
		} else {
			LOG.warning("Unable to read dataframe: " + datasourceName);
			throw new IOException("Unable to read dataframe: " + datasourceName);
		}

		frame = sqlContext.read().load(datasourceName);
		frame.cache();
		setCacheSize((int) frame.count());

		Row[] rows = frame.collect();
		frame.unpersist();
		frame = null;

		for (int i = 0; i < rows.length; i++) {
			process(rows[i]);
			rows[i] = null;
			if (i % 10000 == 0) {
				LOG.info("Processed Rows: " + i);
			}
		}
		LOG.info("Clearing table cache");
		sqlContext.clearCache();
	}

	public final void parse() throws IOException {
		int count = 0;
		for (ROW row : rowCache.valueCollection()) {
			parseFields(row);
			if (count++ % 100000 == 0) {
				LOG.info("Parsed Rows: " + count);
			}
		}
	}

	protected void parseFields(ROW row) {
		// noop
	}

	public final ROW get(int id) {
		return rowCache.get(id);
	}

	public final TIntObjectHashMap<ROW> getCache() {
		return rowCache;
	}

	private static boolean isJoinRightTable(
			BaseTable<? extends TableRow<?>> rightTable, Join join) {
		return rightTable.getClass().isAssignableFrom(join.value());
	}

	private static boolean isJoinOfType(Join join,
			Class<? extends JoinRule> rule) {
		return rule.isAssignableFrom(join.withRule());
	}

	protected void join(ArrayList<ROW> leftRows,
			BaseTable<? extends TableRow<?>> rightTable) {
		if (leftRows.isEmpty() || rightTable.getCache().isEmpty()) {
			LOG.warning("One of the tables is empty. Unable to do join: LT = "
					+ leftRows.size() + ", RT = "
					+ rightTable.getCache().size());
			return;
		}
		Join[] joins = this.getClass().getAnnotationsByType(Join.class);
		for (Join join : joins) {
			long startTime = System.currentTimeMillis();
			if (isJoinRightTable(rightTable, join)) {

				try {
					if (isJoinOfType(join, IdToInstanceJoinRule.class)) {
						LOG.info("[IdToInstance] Left Table= "
								+ getClass().getName() + ", Right Table= "
								+ rightTable.getClass().getName() + ", LF ="
								+ join.leftField() + ", LK=" + join.leftKey());
						TIntObjectHashMap  cache = rightTable
								.getCache();
						IdToInstanceJoinRule joinRule = (IdToInstanceJoinRule) join
								.withRule().newInstance();
						String leftKey = join.leftKey();
						String leftField = join.leftField();

						ROW left = leftRows.get(0);
						final Field foreignKey = left.getClass().getField(
								leftKey);
						Field targetField = left.getClass().getField(leftField);

						int count = 0;
						for (ROW leftRow : leftRows) {
							if (joinRule.join(leftRow, cache, foreignKey,
									targetField)) {
								count++;
							}
						}
						LOG.info("[IdToInstance] Joined rows: " + count
								+ ", Left Count = " + leftRows.size()
								+ ", Right Count = " + cache.size());

					} else if (isJoinOfType(join, LookupJoinRule.class)) {
						LOG.info("[Lookup] Left Table= " + getClass().getName()
								+ ", Right Table= "
								+ rightTable.getClass().getName()
								+ ", Rule Name = " + join.withRule().getName());
						TIntObjectHashMap<? extends TableRow<?>>  cache = rightTable
								.getCache();
						LookupJoinRule<ROW, TIntObjectHashMap<? extends TableRow<?>>> joinRule = (LookupJoinRule<ROW, TIntObjectHashMap<? extends TableRow<?>> >) join
								.withRule().newInstance();
						int count = 0;
						for (ROW leftRow : leftRows) {
							if (joinRule.join(leftRow, cache)) {
								count++;
							}
						}
						LOG.info("[Lookup] Joined rows: " + count
								+ ", Left Count = " + leftRows.size()
								+ ", Right Count = " + cache.size());
					} else if (isJoinOfType(join, NoOpJoinRule.class)) {
						LOG.info("ETL: Join Table = "
								+ join.value().getClass().getName()
								+ ", Left Table= " + getClass().getName()
								+ ", Right Table= "
								+ rightTable.getClass().getName() + ", LF ="
								+ join.leftField() + ", RF ="
								+ join.rightField() + ", LK=" + join.leftKey()
								+ ", RK =" + join.rightKey());

						ArrayList<? extends TableRow<?>> rightRows = rightTable
								.cacheToArray();
						String leftKey = join.leftKey();
						String rightKey = join.rightKey();
						String leftField = join.leftField();
						String rightField = join.rightField();

						ROW left = leftRows.get(0);
						TableRow<?> right = (TableRow<?>) rightRows.get(0);

						final Field fk = left.getClass().getField(leftKey);
						final Field rk = right.getClass().getField(rightKey);
						Field lf = left.getClass().getField(leftField);
						Field rf = right.getClass().getField(rightField);
						long startTimeSort = System.currentTimeMillis();
						LOG.info("Starting sort of left table: "
								+ getClass().getCanonicalName());
						Collections.sort(new ArrayList<ROW>(leftRows),
								new Comparator<ROW>() {

									@Override
									public int compare(ROW o1, ROW o2) {
										try {
											Integer fkInt = (Integer) fk
													.get(o1);
											Integer rkInt = (Integer) fk
													.get(o2);
											if (fkInt.equals(rkInt)) {
												return 0;
											} else if (fkInt < rkInt) {
												return -1;
											} else {
												return 1;
											}
										} catch (Exception e) {
											throw new IllegalArgumentException(
													"Illegal compare values");
										}
									}
								});
						LOG.info("Sort Time: "
								+ (System.currentTimeMillis() - startTimeSort));
						startTimeSort = System.currentTimeMillis();

						LOG.info("Starting sort of right table: "
								+ rightTable.getClass().getCanonicalName());
						Collections.sort(rightRows,
								new Comparator<TableRow<?>>() {

									@Override
									public int compare(TableRow<?> o1,
											TableRow<?> o2) {
										try {
											Integer fkInt = (Integer) rk
													.get(o1);
											Integer rkInt = (Integer) rk
													.get(o2);
											if (fkInt.equals(rkInt)) {
												return 0;
											} else if (fkInt < rkInt) {
												return -1;
											} else {
												return 1;
											}
										} catch (Exception e) {
											throw new IllegalArgumentException(
													"Illegal compare values");
										}
									}

								});
						LOG.info("Sort Time: "
								+ (System.currentTimeMillis() - startTimeSort));

						int start = 0, matchCount = 0;
						for (ROW leftRow : leftRows) {
							for (int i = start; i < rightRows.size(); i++) {
								boolean match = join(leftKey, rightKey,
										leftField, rightField, leftRow,
										rightRows.get(i), fk, rk, lf, rf);
								if (match) {
									start = i;
									matchCount++;
									break;
								}
							}
						}
						LOG.info("[NoOp] Joined rows: " + matchCount);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			LOG.info("ETL: Join time: "
					+ (System.currentTimeMillis() - startTime));
		}
	}

	protected final <LT extends ROW, RT extends TableRow<?>> boolean join(
			String leftKey, String rightKey, String leftField,
			String rightField, LT left, RT right, Field fk, Field rk, Field lf,
			Field rf) throws Exception {
		Integer fkInt = (Integer) fk.get(left);
		Integer rkInt = (Integer) rk.get(right);
		if (fkInt == null || rkInt == null) {
			return true;
		}
		if (fkInt.equals(rkInt)) {
			lf.set(left, rf.get(right));
			return true;
		}
		return false;
	}

	public void joinTables(BaseTable<? extends TableRow<?>>... tables) {
		ArrayList<BaseTable<? extends TableRow<?>>> orderedJoinTables = new ArrayList<>();
		Join[] joins = getClass().getAnnotationsByType(Join.class);

		for (Join join : joins) {
			BaseTable<? extends TableRow<?>> baseTable = getJoinTable(tables,
					join.value());
			if (baseTable != null) {
				orderedJoinTables.add(baseTable);
			} else {
				LOG.warning("Unable to find table specified in join: "
						+ join.value().getName());
			}
		}
		ArrayList<ROW> leftRows = cacheToArray();
		LOG.info("Found tables to join: count = " + orderedJoinTables.size());
		for (BaseTable<? extends TableRow<?>> table : orderedJoinTables) {
			join(leftRows, table);
		}
	}

	protected final <T> T parseField(int field, Row row,
			FieldParser<T> fieldParser) {
		try {
			return fieldParser.parse(field, row);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected final <T> T parseField(String fieldValue,
			FieldParser<T> fieldParser) {
		try {
			return fieldParser.parse(fieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract ROW process(Row row) throws IOException;

	protected final void saveAllToParquetFormat(String tableName, String jdbcUrl) {
		assertValidJdbcScheme(jdbcUrl);
		DataFrame df = sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName).load();
		df.write().save(datasourceName);
	}

	public abstract void saveToParquetFormat(String jdbcUrl);

	protected final void saveToParquetFormat(String tableName,
			Column[] columns, String jdbcUrl) {
		assertValidJdbcScheme(jdbcUrl);
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.select(columns).write().save(datasourceName);
	}

	protected final void saveToParquetFormat(String tableName,
			Column[] columns, String jdbcUrl, int limit) {
		saveToParquetFormat(tableName, columns, jdbcUrl, limit, datasourceName);
	}

	protected final void saveToParquetFormat(String tableName,
			Column[] columns, String jdbcUrl, int limit, String outputName) {
		assertValidJdbcScheme(jdbcUrl);
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.limit(limit).select(columns).write().save(outputName);
	}

	protected final void saveToParquetFormat(String tableName,
			Column[] columns, String jdbcUrl, String outputName) {
		assertValidJdbcScheme(jdbcUrl);
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.select(columns).write().save(outputName);
	}

	private void setCacheSize(int size) {
		rowCache = new TIntObjectHashMap<ROW>(size);
	}

	public final void tranform() {
		if (rowCache != null) {
			LOG.info("Beginning tranform on rows: count = " + rowCache.size());
			int count = 0;
			Iterator<ROW> it = rowCache.valueCollection().iterator();
			while(it.hasNext()) {
				ROW row = it.next();
				transform(row);
				if (count++ % 100000 == 0) {
					LOG.info("Transformed rows: " + count);
				}
				it.remove();
				rowCache.remove(row.id);
			}
		}
	}

	protected void transform(ROW row) {
		// noop
	}
}
