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

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.TableRow;
import org.comicwiki.model.schema.Thing;

import com.google.inject.Inject;

public class StoryTypeTable extends BaseTable<StoryTypeTable.StoryTypeRow> {

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name") };
		public static final int ID = 0;

		public static final int NAME = 1;
	}

	public static class StoryTypeRow extends TableRow<Thing> {

		public String name;
	}

	private static final String sInputTable = "gcd_story_type";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public StoryTypeTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public StoryTypeRow process(Row row) throws IOException {
		StoryTypeRow storyTypeRow = new StoryTypeRow();
		storyTypeRow.name = row.getString(Columns.NAME);
		if (!row.isNullAt(Columns.ID)) {
			storyTypeRow.id = row.getInt(Columns.ID);
			add(storyTypeRow);
		}
		return storyTypeRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

}
