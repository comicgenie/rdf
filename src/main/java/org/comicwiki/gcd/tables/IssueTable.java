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
import java.util.Date;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class IssueTable extends BaseTable<IssueTable.IssueRow> {

	public static final class Columns {

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
		public static final int INDICIA_PUBLISHER_ID = 4;
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

		public String barcode;

		public int brandId;

		public Collection<String> editors = new HashSet<>(3);

		public String indiciaFrequency;

		public int indiciaPublisherId;

		public String isbn;

		public String keyDate;

		public Date modified;

		public String note;

		public String number;

		public String onSaleDate;

		public int pageCount;

		public Collection<String> price = new HashSet<>(3);

		public String publicationDate;

		public String rating;

		public int seriesId;

		public String title;

		public String variantName;

		public String volume;

	}

	private static final String sInputTable = "gcd_issue";

	private static final String sParquetName = sInputTable + ".parquet";

	public IssueTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public IssueRow process(Row row) throws IOException {
		IssueRow issueRow = new IssueRow();
		issueRow.barcode = row.getString(Columns.BARCODE);
		issueRow.brandId = row.getInt(Columns.BRAND_ID);
		issueRow.id = row.getInt(Columns.ID);
		issueRow.indiciaFrequency = row.getString(Columns.INDICIA_FREQUENCY);
		issueRow.indiciaPublisherId = row.getInt(Columns.INDICIA_PUBLISHER_ID);
		issueRow.isbn = row.getString(Columns.ISBN);
		issueRow.keyDate = row.getString(Columns.KEY_DATE);
		issueRow.modified = row.getTimestamp(Columns.MODIFIED);
		issueRow.note = row.getString(Columns.NOTES);
		issueRow.number = row.getString(Columns.NUMBER);
		issueRow.onSaleDate = row.getString(Columns.ON_SALE_DATE);
		issueRow.pageCount = row.getInt(Columns.PAGE_COUNT);
		issueRow.publicationDate = row.getString(Columns.PUBLICATION_DATE);
		issueRow.rating = row.getString(Columns.RATING);
		issueRow.seriesId = row.getInt(Columns.SERIES_ID);
		issueRow.title = row.getString(Columns.TITLE);
		issueRow.variantName = row.getString(Columns.VARIANT_NAME);
		issueRow.volume = row.getString(Columns.VOLUMN);

		if (!row.isNullAt(Columns.PRICE)) {
			issueRow.price = parseField(
					Columns.PRICE,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings().split(r.getString(f)));
					});
		}
		if (!row.isNullAt(Columns.EDITING)) {
			// issueRow.editors
		}
		return issueRow;

	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

}
