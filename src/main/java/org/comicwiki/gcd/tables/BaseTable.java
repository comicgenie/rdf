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
package org.comicwiki.gcd.tables;

import java.io.IOException;
import java.util.HashMap;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import org.comicwiki.gcd.FieldParser;

public abstract class BaseTable<R extends TableRow> {

	protected HashMap<Integer, R> cache = new HashMap<>();

	protected final String datasourceName;

	protected DataFrame frame;

	protected final SQLContext sqlContext;

	public BaseTable(SQLContext sqlContext, String datasourceName) {
		this.sqlContext = sqlContext;
		this.datasourceName = datasourceName;
	}

	public final void add(R row) {
		cache.put(row.id, row);
	}

	public void assignRelations(R row) {
		// noop
	}

	private DataFrameReader createReader(String jdbcUrl, String tableName) {
		return sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName);
	}

	public void exportRowToRepositories(R row) {//make abstract

	}

	public final void exportTableToRepositories() {
		cache.values().forEach(r -> exportRowToRepositories(r));
	}

	public final R get(int id) {
		return cache.get(id);
	}

	public final void load() throws IOException {
		frame = sqlContext.read().load(datasourceName);
		frame.persist(StorageLevel.MEMORY_AND_DISK_SER());

		setCacheSize((int) frame.count());

		for (Row row : frame.collect()) {
			process(row);
		}
	}

	public final <T> T parseField(int field, Row row, FieldParser<T> fieldParser) {
		return fieldParser.parse(field, row);
	}

	public abstract R process(Row row) throws IOException;

	protected final void saveAllToParquetFormat(String tableName, String jdbcUrl) {
		DataFrame df = sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName).load();
		df.write().save(datasourceName);
	}

	public abstract void saveToParquetFormat(String jdbcUrl);

	protected final void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl) {
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.select(columns).write().save(datasourceName);
	}

	protected final void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl, int limit) {
		saveToParquetFormat(tableName, columns, jdbcUrl, limit, datasourceName);
	}

	protected final void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl, int limit, String outputName) {
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.limit(limit).select(columns).write().save(outputName);
	}

	protected final void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl, String outputName) {
		DataFrame df = createReader(jdbcUrl, tableName).load();
		df.select(columns).write().save(outputName);
	}

	protected final void setCacheSize(int size) {
		cache = new HashMap<Integer, R>(size);
	}
}
