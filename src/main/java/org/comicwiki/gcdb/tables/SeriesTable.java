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

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class SeriesTable extends BaseTable<SeriesTable.SeriesRow> {

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"), new Column("format"),
				new Column("year_began"), new Column("year_ended"),
				new Column("publication_dates"), new Column("publisher_id"),
				new Column("country_id"), new Column("language_id"),
				new Column("tracking_notes"), new Column("notes"),
				new Column("publication_notes"), new Column("modified"),
				new Column("color"), new Column("dimensions"),
				new Column("paper_stock"), new Column("binding"),
				new Column("publishing_format") };
		public static final int BINDING = 17;
		public static final int COLOR = 13;
		public static final int COUNTRY_ID = 7;
		public static final int DIMENSIONS = 14;
		public static final int FORMAT = 2;
		public static final int ID = 0;
		public static final int LANGUAGE_ID = 8;
		public static final int MODIFIED = 12;
		public static final int NAME = 1;
		public static final int NOTES = 10;
		public static final int PAPER_STOCK = 16;
		public static final int PUBLICATION_DATES = 5;
		public static final int PUBLICATION_NOTES = 11;
		public static final int PUBLISHER_ID = 6;
		public static final int PUBLISHING_FORMAT = 18;
		public static final int REPRINT_NOTES = 15;
		public static final int TRACKING_NOTES = 9;
		public static final int YEAR_BEGAN = 3;

		public static final int YEAR_ENDED = 4;
	}

	public static class SeriesRow extends TableRow {

	}

	private static final String sTableName = "gcd_series";
	
	private static final String sParquetName = sTableName + ".parquet";

	public SeriesTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public void join(SeriesRow row) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public SeriesRow process(Row row) throws IOException {
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sTableName, Columns.ALL_COLUMNS, jdbcUrl);

	}

}
