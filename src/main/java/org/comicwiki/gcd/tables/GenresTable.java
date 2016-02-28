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
import java.util.Collection;
import java.util.HashMap;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.Genre;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class GenresTable extends BaseTable<GenresTable.GenreRow> {

	private static final class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] { new Column(
				"genre") };
	}

	public class GenreRow extends TableRow<Genre> {
		//public Genre instance = create(thingFactory);
	}

	private static final String sParquetName = "genres.parquet";

	private static final String sTableName = "gcd_story";
	
	public HashMap<String, Genre> cache = new HashMap<>();

	private ThingFactory thingFactory;

	@Inject
	public GenresTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
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
		
		for(String name : genres) {
			if(!cache.containsKey(name)) {
				Genre genre = thingFactory.create(Genre.class);
				genre.name = name;	
				cache.put(name, genre);
			}
		}
		
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sTableName, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
