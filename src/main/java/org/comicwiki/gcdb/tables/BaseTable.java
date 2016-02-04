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
package org.comicwiki.gcdb.tables;

import java.io.IOException;
import java.util.HashMap;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcdb.fields.FieldParser;

public abstract class BaseTable<R> {

	protected HashMap<Integer, R> cache = new HashMap<>();

	private final String datasourceName;

	protected final SQLContext sqlContext;

	public BaseTable(SQLContext sqlContext, String datasourceName) {
		this.sqlContext = sqlContext;
		this.datasourceName = datasourceName;
	}

	public void add(int id, R row) {
		cache.put(id, row);
		process(row);
	}

	public R get(int id) {
		return cache.get(id);
	}

	public void join() throws IOException {
		for (R r : cache.values()) {
			join(r);
		}
	}

	public abstract void join(R row) throws IOException;

	public void load() throws IOException {
		DataFrame frame = sqlContext.read().load(datasourceName);
		setCacheSize((int) frame.count());

		for (Row row : frame.collect()) {
			process(row);
		}
	}

	public <T> T parseField(int field, Row row, FieldParser<T> fieldParser) {
		return fieldParser.parse(field, row);
	}

	public void process(R row) {
		// noop
	}

	public abstract R process(Row row) throws IOException;

	public void rowsToRepositories() {

	}

	protected void saveAllToParquetFormat(String tableName, String jdbcUrl) {
		DataFrame df = sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName).load();
		df.write().save(datasourceName);
	}

	public abstract void saveToParquetFormat(String jdbcUrl);

	protected void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl) {
		DataFrame df = sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName).load();
		df.select(columns).write().save(datasourceName);
	}

	protected void saveToParquetFormat(String tableName, Column[] columns,
			String jdbcUrl, int limit) {
		DataFrame df = sqlContext.read().format("jdbc").option("url", jdbcUrl)
				.option("dbtable", tableName).load();
		df.limit(limit).select(columns).write().save(datasourceName);
	}

	private void setCacheSize(int size) {
		cache = new HashMap<Integer, R>(size);
	}
}
