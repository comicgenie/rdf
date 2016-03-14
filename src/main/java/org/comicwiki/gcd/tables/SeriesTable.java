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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.Add;
import org.comicwiki.BaseTable;
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.joinrules.IdToInstanceJoinRule;
import org.comicwiki.model.Instant;
import org.comicwiki.model.TemporalEntity;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Language;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.bib.ComicSeries;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Join(value = LanguageTable.class, leftKey = "fkLanguageId", leftField = "language", withRule=IdToInstanceJoinRule.class)
@Join(value = CountryTable.class, leftKey = "fkCountryId", leftField = "country", withRule=IdToInstanceJoinRule.class)
@Join(value = PublisherTable.class, leftKey = "fkPublisherId", leftField = "publisher", withRule=IdToInstanceJoinRule.class)
@Join(value = SeriesPublicationTypeTable.class, leftKey = "fkPublicationTypeId", leftField = "publicationType", rightField = "name")
@Singleton
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
				new Column("publishing_format"),
				new Column("publication_type_id") };
		public static final int BINDING = 16;
		public static final int COLOR = 13;
		public static final int COUNTRY_ID = 7;
		public static final int DIMENSIONS = 14;
		public static final int FORMAT = 2;
		public static final int ID = 0;
		public static final int LANGUAGE_ID = 8;
		public static final int MODIFIED = 12;
		public static final int NAME = 1;
		public static final int NOTES = 10;
		public static final int PAPER_STOCK = 15;
		public static final int PUBLICATION_DATE = 5;
		public static final int PUBLICATION_NOTES = 11;// unused
		public static final int PUBLISHER_ID = 6;
		public static final int PUBLISHING_FORMAT = 17;
		public static final int PUBLICATION_TYPE_ID = 18;
		public static final int TRACKING_NOTES = 9;
		public static final int YEAR_BEGAN = 3;
		public static final int YEAR_ENDED = 4;
		// public static final int KEY_DATE = 20;
	}

	public static class SeriesRow extends TableRow<ComicSeries> {

		public String[] binding;

		public String[] color;

		public Country country;

		public String[] dimensions;

		/**
		 * gcd_country.id
		 */
		public int fkCountryId;

		/**
		 * gcd_language.id
		 */
		public int fkLanguageId;

		/**
		 * gcd_publisher.id
		 */
		public int fkPublisherId;

		/**
		 * gcd_publication_type.id
		 */
		public int fkPublicationTypeId;

		public String publicationType;

		public String[] format;

		public ComicSeries instance = create(thingFactory);

		public Language language;

		public Date modified;

		public String name;

		public String notes;

		public String[] paperStock;

		public TemporalEntity publicationDate;

		public String publicationNotes;

		public Organization publisher;

		public String[] publishingFormat;

		public String trackingNotes;

		public Integer yearBegan;

		public Integer yearEnded;

	}

	private static final String sInputTable = "gcd_series";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	private FieldParserFactory parserFactory;

	@Inject
	public SeriesTable(SQLContext sqlContext, ThingFactory thingFactory,
			FieldParserFactory parserFactory) {
		super(sqlContext, sParquetName);
		SeriesTable.thingFactory = thingFactory;
		this.parserFactory = parserFactory;
	}

	public SeriesRow createRow() {
		return new SeriesRow();
	}

	@Override
	public SeriesRow process(Row row) throws IOException {
		SeriesRow seriesRow = new SeriesRow();

		if (!row.isNullAt(Columns.COUNTRY_ID)) {
			seriesRow.fkCountryId = row.getInt(Columns.COUNTRY_ID);
		}
		if (!row.isNullAt(Columns.LANGUAGE_ID)) {
			seriesRow.fkLanguageId = row.getInt(Columns.LANGUAGE_ID);
		}

		seriesRow.modified = row.getTimestamp(Columns.MODIFIED);
		seriesRow.name = row.getString(Columns.NAME);
		seriesRow.name = row.getString(Columns.NAME);

		seriesRow.instance.name = row.getString(Columns.NAME);

		seriesRow.notes = row.getString(Columns.NOTES);
		seriesRow.publicationNotes = row.getString(Columns.PUBLICATION_NOTES);

		if (!row.isNullAt(Columns.PUBLICATION_DATE)) {
			seriesRow.publicationDate = parseField(Columns.PUBLICATION_DATE,
					row, parserFactory.publishDate());
		}

		// if (!row.isNullAt(Columns.KEY_DATE)) {
		// seriesRow.publicationDates = parseField(Columns.PUBLICATION_DATES,
		// row, parserFactory.publishDate());
		// }

		// TODO: xdsf

		if (!row.isNullAt(Columns.PUBLISHER_ID)) {
			seriesRow.fkPublisherId = row.getInt(Columns.PUBLISHER_ID);
		}

		seriesRow.trackingNotes = row.getString(Columns.TRACKING_NOTES);

		if (!row.isNullAt(Columns.YEAR_BEGAN)) {
			seriesRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		}

		if (!row.isNullAt(Columns.YEAR_ENDED)) {
			seriesRow.yearEnded = row.getInt(Columns.YEAR_ENDED);
		}

		if (!row.isNullAt(Columns.FORMAT)) {
			seriesRow.format = Add
					.toArray(
							parseField(Columns.FORMAT, row,
									parserFactory.string(false)), String.class);
		}

		if (!row.isNullAt(Columns.COLOR)) {
			seriesRow.color = Add
					.toArray(
							parseField(Columns.COLOR, row,
									parserFactory.string(false)), String.class);
		}

		if (!row.isNullAt(Columns.BINDING)) {
			seriesRow.binding = Add.toArray(
					parseField(Columns.BINDING, row,
							parserFactory.string(false)), String.class);
		}

		if (!row.isNullAt(Columns.DIMENSIONS)) {
			seriesRow.dimensions = Add.toArray(
					parseField(Columns.DIMENSIONS, row,
							parserFactory.string(false)), String.class);
		}

		if (!row.isNullAt(Columns.PAPER_STOCK)) {
			seriesRow.paperStock = Add.toArray(
					parseField(Columns.PAPER_STOCK, row,
							parserFactory.string(false)), String.class);
		}

		if (!row.isNullAt(Columns.PUBLISHING_FORMAT)) {
			try {
				seriesRow.publishingFormat = Add.toArray(
						parseField(Columns.PUBLISHING_FORMAT, row,
								parserFactory.string(false)), String.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!row.isNullAt(Columns.ID)) {
			seriesRow.id = row.getInt(Columns.ID);
			add(seriesRow);
		}
		return seriesRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(SeriesRow row) {
		super.transform(row);

		ComicSeries series = row.instance;
		try {
			series.addUrl(new URL("http://www.comics.org/series/" + row.id));
			series.addUrl(new URL("http://www.comics.org/series/" + row.id
					+ "/covers"));
		} catch (MalformedURLException e) {
		}

		if (Strings.isNullOrEmpty(series.name)) {
			System.out.println("ETL: null series name " + row.id);
		}
		// series.authors

		series.binding = Add.both(series.binding, row.binding, String.class);
		series.binding = Add.both(series.binding, row.format, String.class);
		series.colors = Add.both(series.colors, row.color, String.class);
		series.dimensions = Add.both(series.dimensions, row.dimensions,
				String.class);
		series.format = Add.both(series.format, row.publishingFormat,
				String.class);
		series.paperStock = Add.both(series.paperStock, row.paperStock,
				String.class);
		if (!Strings.isNullOrEmpty(row.publicationType)) {
			series.periodicalType = row.publicationType;
		}
		if (row.country != null) {
			series.locationCreated = row.country.instanceId;
		}

		if (row.language != null) {
			series.inLanguage = row.language.instanceId;
		}

		if (row.publisher != null) {
			series.addPublisher(row.publisher.instanceId);
		}

		if (row.publicationDate != null) {
			series.datePublished = row.publicationDate.instanceId;
		}

		if (row.yearBegan != null) {
			Instant begin = thingFactory.create(Instant.class);
			begin.year = row.yearBegan;
			row.instance.startDate = begin.instanceId;
		}

		if (row.yearEnded != null) {
			Instant end = thingFactory.create(Instant.class);
			end.year = row.yearEnded;
			row.instance.endDate = end.instanceId;
		}

		if (!Strings.isNullOrEmpty(row.notes)) {
			row.instance.addDescription(row.notes);
		}
	}
}
