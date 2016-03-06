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
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.model.Instant;
import org.comicwiki.model.TemporalEntity;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Language;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.bib.ComicSeries;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

@Join(value = LanguageTable.class, leftKey = "fkLanguageId", leftField = "language")
@Join(value = CountryTable.class, leftKey = "fkCountryId", leftField = "country")
@Join(value = PublisherTable.class, leftKey = "fkPublisherId", leftField = "publisher")
@Join(value = SeriesPublicationTypeTable.class, leftKey = "fkPublicationTypeId", leftField = "publicationType", rightField = "name")
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
		public static final int PUBLICATION_NOTES = 11;// unused
		public static final int PUBLISHER_ID = 6;
		public static final int PUBLISHING_FORMAT = 18;
		public static final int PUBLICATION_TYPE_ID = 19;
		public static final int REPRINT_NOTES = 15;
		public static final int TRACKING_NOTES = 9;
		public static final int YEAR_BEGAN = 3;
		public static final int YEAR_ENDED = 4;
	}

	public static class SeriesRow extends TableRow<ComicSeries> {

		public Collection<String> binding = new HashSet<>(3);

		public Collection<String> color = new HashSet<>(3);

		public Country country;

		public Collection<String> dimensions = new HashSet<>(3);

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

		public Collection<String> format = new HashSet<>(3);

		public ComicSeries instance = create(thingFactory);

		public Language language;

		public Date modified;

		public String name;

		public String notes;

		public Collection<String> paperStock = new HashSet<>(3);

		public TemporalEntity publicationDates;

		public String publicationNotes;

		public Organization publisher;

		public Collection<String> publishingFormat = new HashSet<>(3);

		public String trackingNotes;

		public Integer yearBegan;

		public Integer yearEnded;

	}

	private static final String sInputTable = "gcd_series";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	private static Collection<String> split(int position, Row r) {
		return Sets.newHashSet(Splitter.on(';').trimResults()
				.omitEmptyStrings().split(r.getString(position)));
	}

	private FieldParserFactory parserFactory;

	@Inject
	public SeriesTable(SQLContext sqlContext, ThingFactory thingFactory,
			FieldParserFactory parserFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
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
		seriesRow.instance.name = row.getString(Columns.NAME);

		seriesRow.notes = row.getString(Columns.NOTES);
		seriesRow.publicationNotes = row.getString(Columns.PUBLICATION_NOTES);

		if (!row.isNullAt(Columns.PUBLICATION_DATES)) {
			seriesRow.publicationDates = parseField(Columns.PUBLICATION_DATES,
					row, parserFactory.publishDate());
		}

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
		series.urls.add(URI.create("http://www.comics.org/series/" + row.id));
		series.urls.add(URI.create("http://www.comics.org/series/" + row.id
				+ "/covers"));

		series.name = row.name;
		// series.authors

		series.binding.addAll(row.binding);
		series.binding.addAll(row.format);
		series.colors.addAll(row.color);
		series.dimensions.addAll(row.dimensions);
		series.format.addAll(row.publishingFormat);
		series.paperStock.addAll(row.paperStock);
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
			series.publishers.add(row.publisher.instanceId);
		}

		if (row.publicationDates != null) {
			series.datePublished = row.publicationDates.instanceId;
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
			row.instance.description.add(row.notes);
		}
	}
}
