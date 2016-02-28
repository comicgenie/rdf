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
import org.comicwiki.BaseTable;
import org.comicwiki.model.schema.bib.ComicSeries;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

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

	public static class SeriesRow extends TableRow<ComicSeries> {

		public Collection<String> binding = new HashSet<>(3);

		public Collection<String> color = new HashSet<>(3);

		/**
		 * gcd_country.id
		 */
		public int countryId;

		public String country;

		public Collection<String> dimensions = new HashSet<>(3);

		public Collection<String> format = new HashSet<>(3);

		/**
		 * gcd_language.id
		 */
		public int languageId;

		public String language;

		public Date modified;

		public String name;

		public String notes;

		public Collection<String> paperStock = new HashSet<>(3);

		public String publicationDates;

		public String publicationNotes;

		/**
		 * gcd_publisher.id
		 */
		public int publisherId;

		public String publisher;

		public Collection<String> publishingFormat = new HashSet<>(3);

		public String trackingNotes;

		public int yearBegan;

		public int yearEnded;

	}

	private static final String sInputTable = "gcd_series";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public SeriesTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	protected void transform(SeriesRow row) {
		super.transform(row);
	}

	@Override
	public void join(BaseTable<?>... tables) {
		super.join(tables);
	}

	@Override
	protected void join(BaseTable<?> table) {
		super.join(table);
		// publisherId
		// countryId
		// languageId
	}

	@Override
	public SeriesRow process(Row row) throws IOException {
		SeriesRow seriesRow = new SeriesRow();

		if (!row.isNullAt(Columns.COUNTRY_ID)) {
			seriesRow.countryId = row.getInt(Columns.COUNTRY_ID);
		}
		if (!row.isNullAt(Columns.LANGUAGE_ID)) {
			seriesRow.languageId = row.getInt(Columns.LANGUAGE_ID);
		}

		seriesRow.modified = row.getTimestamp(Columns.MODIFIED);
		seriesRow.name = row.getString(Columns.NAME);
		seriesRow.notes = row.getString(Columns.NOTES);
		seriesRow.publicationDates = row.getString(Columns.PUBLICATION_DATES);
		seriesRow.publicationNotes = row.getString(Columns.PUBLICATION_NOTES);

		if (!row.isNullAt(Columns.PUBLISHER_ID)) {
			seriesRow.publisherId = row.getInt(Columns.PUBLISHER_ID);
		}

		seriesRow.trackingNotes = row.getString(Columns.TRACKING_NOTES);

		if (!row.isNullAt(Columns.YEAR_BEGAN)) {
			seriesRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		}

		if (!row.isNullAt(Columns.YEAR_ENDED)) {
			seriesRow.yearEnded = row.getInt(Columns.YEAR_ENDED);
		}

		if (!row.isNullAt(Columns.FORMAT)) {
			seriesRow.format = parseField(Columns.FORMAT, row,
					(f, r) -> split(f, r));
		}

		if (!row.isNullAt(Columns.COLOR)) {
			seriesRow.color = parseField(Columns.COLOR, row,
					(f, r) -> split(f, r));
		}

		if (!row.isNullAt(Columns.BINDING)) {
			seriesRow.binding = parseField(Columns.BINDING, row,
					(f, r) -> split(f, r));
		}

		if (!row.isNullAt(Columns.DIMENSIONS)) {
			seriesRow.dimensions = parseField(Columns.DIMENSIONS, row,
					(f, r) -> split(f, r));
		}

		if (!row.isNullAt(Columns.PAPER_STOCK)) {
			seriesRow.paperStock = parseField(Columns.PAPER_STOCK, row,
					(f, r) -> split(f, r));
		}

		if (!row.isNullAt(Columns.PUBLISHING_FORMAT)) {
			seriesRow.publishingFormat = parseField(Columns.PUBLISHING_FORMAT,
					row, (f, r) -> split(f, r));
		}
		if (!row.isNullAt(Columns.ID)) {
			seriesRow.id = row.getInt(Columns.ID);
			;
			add(seriesRow);
		}
		return seriesRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);

	}

	private static Collection<String> split(int position, Row r) {
		return Sets.newHashSet(Splitter.on(';').trimResults()
				.omitEmptyStrings().split(r.getString(position)));
	}

}
