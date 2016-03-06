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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.gcd.SparkUtils;
import org.comicwiki.model.schema.Thing;

import com.google.inject.Singleton;

@Singleton
public abstract class BaseTable<ROW extends TableRow<?>> {

	private static void assertValidJdbcScheme(String jdbcUrl) {
		if (!SparkUtils.isValidScheme(jdbcUrl)) {
			throw new IllegalArgumentException("invalid jdbc scheme: "
					+ jdbcUrl);
		}
	}

	public HashMap<Integer, ROW> cache = new HashMap<>();

	protected final String datasourceName;

	protected DataFrame frame;

	protected HashMap<String, Thing> instanceCache = new HashMap<>(1000);

	protected final SQLContext sqlContext;

	private final TableFormat tableFormat;

	public BaseTable(SQLContext sqlContext, String datasourceName) {
		this(sqlContext, datasourceName, TableFormat.RDB);
	}

	public BaseTable(SQLContext sqlContext, String datasourceName,
			TableFormat tableFormat) {
		this.sqlContext = sqlContext;
		this.datasourceName = datasourceName;
		this.tableFormat = tableFormat;
	}

	protected final void add(ROW row) {
		cache.put(row.id, row);
	}

	private DataFrameReader createReader(String jdbcUrl, String tableName) {
		return sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName);
	}

	public final void extract() throws IOException {
		if (sqlContext == null) {
			return;// noop
		}
		if (TableFormat.RDB.equals(tableFormat)) {
			frame = sqlContext.read().load(datasourceName);
		} else if (TableFormat.JSON.equals(tableFormat)) {
			frame = sqlContext.read().json(datasourceName);
		} else {
			throw new IOException("Unable to read dataframe: " + datasourceName);
		}

		frame = sqlContext.read().load(datasourceName);
		frame.persist(StorageLevel.MEMORY_AND_DISK_SER());

		setCacheSize((int) frame.count());

		for (Row row : frame.collect()) {
			process(row);
		}
	}

	public final ROW get(int id) {
		return cache.get(id);
	}

	public final HashMap<Integer, ROW> getCache() {
		return cache;
	}

	private static BaseTable getTable(BaseTable[] tables, Class<?> clazz) {
		for (BaseTable baseTable : tables) {
			if (baseTable.getClass().isAssignableFrom(clazz)) {
				return baseTable;
			}
		}
		return null;
	}

	public void join(BaseTable<?>... tables) {
		ArrayList<BaseTable<?>> orderedTables = new ArrayList<>();
		Join[] joins = getClass().getAnnotationsByType(Join.class);

		for (Join join : joins) {
			BaseTable baseTable = getTable(tables, join.value());
			if (baseTable != null) {
				orderedTables.add(baseTable);
			}
		}

		for (BaseTable<?> table : orderedTables) {
			join(table);
		}
	}

	protected void join(BaseTable<?> table) {
		Join[] joins = this.getClass().getAnnotationsByType(Join.class);
		for (Join join : joins) {
			if (table.getClass().isAssignableFrom(join.value())) {
				try {
					if (join.withRule().isAssignableFrom(NoOpJoinRule.class)) {
						for (TableRow<?> rightRow : table.getCache().values()) {
							for (ROW leftRow : getCache().values()) {
								join(join.leftKey(), join.rightKey(),
										join.leftField(), join.rightField(),
										leftRow, rightRow);
							}
						}
					} else {
						join(this, table, join.withRule().newInstance());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected final <LT extends ROW, RT extends TableRow<?>> void join(
			String leftKey, String rightKey, String leftField,
			String rightField, LT left, RT right) throws Exception {
		Field fk = left.getClass().getField(leftKey);
		Field rk = right.getClass().getField(rightKey);
		Field lf = left.getClass().getField(leftField);
		Field rf = right.getClass().getField(rightField);
		if (fk.getInt(left) == rk.getInt(right)) {
			lf.set(left, rf.get(right));
		}
	}

	protected final <LT extends BaseTable<ROW>, RT extends BaseTable<?>, JR extends JoinRule> void join(
			LT left, RT right, JR rule) {
		for (TableRow<?> rightRow : right.getCache().values()) {
			left.getCache()
					.values()
					.forEach(
							leftJoinedRow -> rule.join(leftJoinedRow, rightRow));
		}
	}

	protected final <T> T parseField(int field, Row row,
			FieldParser<T> fieldParser) {
		return fieldParser.parse(field, row);
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
		cache = new HashMap<Integer, ROW>(size);
	}

	public final void tranform() {
		if (cache != null) {
			cache.values().forEach(r -> transform(r));
		}
	}

	protected void transform(ROW row) {
		// noop
	}
}
