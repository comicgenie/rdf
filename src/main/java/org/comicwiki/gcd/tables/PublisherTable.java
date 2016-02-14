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
import java.util.Date;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.model.schema.Organization;

public class PublisherTable extends BaseTable<PublisherTable.PublisherRow> {
	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"), new Column("country_id"),
				new Column("year_began"), new Column("year_ended"),
				new Column("notes"), new Column("url"), new Column("modified") };

		public static final int COUNTRY_ID = 2;

		public static final int ID = 0;

		public static final int MODIFIED = 7;

		public static final int NAME = 1;

		public static final int NOTES = 5;

		public static final int URL = 6;
		public static final int YEAR_BEGAN = 3;
		public static final int YEAR_ENDED = 4;
	}

	public static class PublisherRow extends TableRow<Organization> {

		public int countryId;

		public Date modified;

		public String name;

		public String notes;

		public String url;

		public int yearBegan;

		public int yearEnded;
	}

	private static final String sInputTable = "gcd_publisher";

	private static final String sParquetName = sInputTable + ".parquet";

	public PublisherTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public PublisherRow process(Row row) throws IOException {
		PublisherRow publisherRow = new PublisherRow();
		publisherRow.id = row.getInt(Columns.ID);
		publisherRow.countryId = row.getInt(Columns.COUNTRY_ID);
		publisherRow.modified = row.getTimestamp(Columns.MODIFIED);
		publisherRow.name = row.getString(Columns.NAME);
		publisherRow.notes = row.getString(Columns.NOTES);
		publisherRow.url = row.getString(Columns.URL);
		publisherRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		publisherRow.yearEnded = row.getInt(Columns.YEAR_ENDED);

		add(publisherRow);
		return publisherRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
