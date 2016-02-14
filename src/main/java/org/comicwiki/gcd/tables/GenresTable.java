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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class GenresTable extends BaseTable<GenresTable.GenreRow> {

	private static final class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] { new Column(
				"genre") };
	}

	public static class GenreRow extends TableRow {
		String gcdName;

		String name;
	}

	private static final String sParquetName = "genres.parquet";

	private static final String sTableName = "gcd_story";

	public TreeSet<String> cache = new TreeSet<>();

	HashMap<String, Integer> g2 = new HashMap<>();

	public GenresTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public GenreRow process(Row row) throws IOException {
		Collection<String> genres = parseField(
				0,
				row,
				(f, r) -> {
					return Sets.newHashSet(Splitter.on(';').trimResults()
							.omitEmptyStrings()
							.split(r.getString(f).toUpperCase()));
				});
		for (String gen : genres) {
			if (g2.containsKey(gen)) {
				int value = g2.get(gen) + 1;
				g2.put(gen, value);
			} else {
				g2.put(gen, 1);
			}
		}
		cache.addAll(genres);
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sTableName, Columns.ALL_COLUMNS, jdbcUrl);
	}

	public void writeToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		for (String genre : cache) {
			fos.write((genre + "\r\n").getBytes());
		}
		fos.close();
		//System.out.println(g2);
	}
}
