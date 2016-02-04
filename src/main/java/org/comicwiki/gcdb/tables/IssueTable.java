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
import java.util.Collection;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcdb.Join;

public class IssueTable extends BaseTable<IssueTable.IssueRow> {

	private static final class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("number"), new Column("volume"),
				new Column("series_id"), new Column("indicia_publisher_id"),
				new Column("brand_id"), new Column("publication_date"),
				new Column("key_date"), new Column("price"),
				new Column("page_count"), new Column("indicia_frequency"),
				new Column("editing"), new Column("notes"),
				new Column("modified"), new Column("valid_isbn"),
				new Column("variant_name"), new Column("barcode"),
				new Column("title"), new Column("on_sale_date"),
				new Column("rating") };
		public static final int BARCODE = 16;
		public static final int BRAND_ID = 5;
		public static final int EDITING = 11;
		public static final int ID = 0;
		public static final int INDICIA_FREQUENCY = 10;
		public static final int indicia_publisher_id = 4;
		public static final int ISBN = 14;
		public static final int KEY_DATE = 7;
		public static final int MODIFIED = 13;
		public static final int NOTES = 12;
		public static final int NUMBER = 1;
		public static final int ON_SALE_DATE = 18;
		public static final int PAGE_COUNT = 9;
		public static final int PRICE = 8;
		public static final int PUBLICATION_DATE = 6;
		public static final int RATING = 19;
		public static final int SERIES_ID = 3;
		public static final int TITLE = 17;
		public static final int VARIANT_NAME = 15;
		public static final int VOLUMN = 2;

	}

	public static class Fields {

	}

	public static class IssueRow extends TableRow {

		Collection<String> editors;

		@Join(field = "series_id", table = IssueTable.class)
		// SeriesTable
		public int seriesId;
	}

	private static final String sTableName = "gcd_issue";

	private static final String sParquetName = sTableName + ".parquet";

	public IssueTable(SQLContext sqlContext, String datasourceName) {
		super(sqlContext, datasourceName);
	}

	@Override
	public void join(IssueRow row) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public IssueRow process(Row row) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sTableName, Columns.ALL_COLUMNS, jdbcUrl);

	}

}
