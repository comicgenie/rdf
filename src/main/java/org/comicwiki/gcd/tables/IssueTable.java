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
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.CreatorField;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.joinrules.IssueAndSeriesRule;
import org.comicwiki.model.ComicIssueNumber;
import org.comicwiki.model.CreatorAlias;
import org.comicwiki.model.CreatorRole;
import org.comicwiki.model.Instant;
import org.comicwiki.model.prices.Price;
import org.comicwiki.model.schema.Brand;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.PublicationVolume;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.comicwiki.model.schema.bib.ComicSeries;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Join(value = SeriesTable.class, withRule = IssueAndSeriesRule.class)
@Join(value = BrandTable.class, leftKey = "fkBrandId", leftField = "brand")
@Join(value = IndiciaPublisherTable.class, leftKey = "fkIndiciaPublisherId", leftField = "indiciaPublisher")
@Join(value = SeriesTable.class, leftKey = "fkSeriesId", leftField = "series")
@Join(value = PublisherTable.class, leftKey = "fkPublisherId", leftField = "publisher")
@Singleton
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
		public static final int VOLUME = 2;
	}

	public static class Fields {

	}

	public static class IssueRow extends TableRow<ComicIssue> {

		public Collection<Person> alaises;

		public String barcode;

		public Brand brand;

		public Collection<CreatorAlias> creatorAliases;

		public Collection<Person> editors;

		/**
		 * gcd_brand.id
		 */
		public int fkBrandId;

		/**
		 * gcd_indicia_publisher.id
		 */
		public int fkIndiciaPublisherId;

		/**
		 * 
		 */
		public int fkPublisherId;

		/**
		 * gcd_series.id
		 */
		public int fkSeriesId;

		public String indiciaFrequency;

		public Organization indiciaPublisher;

		public ComicIssue instance = create(thingFactory);

		public String isbn;

		public String keyDate;

		public Date modified;

		public String note;

		public ComicIssueNumber number;

		public Instant onSaleDate;

		public int pageCount;

		public Collection<Price> price;

		public String publicationDate;

		public Organization publisher;

		public String rating;

		public ComicSeries series;

		public String title;

		public String variantName;

		public String volume;

	}

	private static final String sInputTable = "gcd_issue";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	private FieldParserFactory parserFactory;

	@Inject
	public IssueTable(SQLContext sqlContext, ThingFactory thingFactory,
			FieldParserFactory parserFactory) {
		super(sqlContext, sParquetName);
		IssueTable.thingFactory = thingFactory;
		this.parserFactory = parserFactory;
	}

	private IRI createInstant(String keyDate, String datePublished) {
		Instant datePublisher = thingFactory.create(Instant.class);
		return datePublisher.instanceId;
	}

	/**
	 * 
	 isbn; ComicSeries series
	 */

	@Override
	public IssueRow process(Row row) throws IOException {
		IssueRow issueRow = new IssueRow();
		issueRow.barcode = row.getString(Columns.BARCODE);
		if (!row.isNullAt(Columns.BRAND_ID)) {
			issueRow.fkBrandId = row.getInt(Columns.BRAND_ID);
		}

		issueRow.indiciaFrequency = row.getString(Columns.INDICIA_FREQUENCY);
		if (!row.isNullAt(Columns.INDICIA_PUBLISHER_ID)) {
			issueRow.fkIndiciaPublisherId = row
					.getInt(Columns.INDICIA_PUBLISHER_ID);
		}
		issueRow.isbn = row.getString(Columns.ISBN);
		issueRow.keyDate = row.getString(Columns.KEY_DATE);
		issueRow.modified = row.getTimestamp(Columns.MODIFIED);
		issueRow.note = row.getString(Columns.NOTES);
		if (!row.isNullAt(Columns.ON_SALE_DATE)) {
			issueRow.onSaleDate = parseField(Columns.ON_SALE_DATE, row,
					parserFactory.saleDate());
		}

		if (!row.isNullAt(Columns.PAGE_COUNT)) {
			issueRow.pageCount = ((BigDecimal) row.get(Columns.PAGE_COUNT))
					.intValue();
		}

		issueRow.publicationDate = row.getString(Columns.PUBLICATION_DATE);
		issueRow.rating = row.getString(Columns.RATING);
		if (!row.isNullAt(Columns.SERIES_ID)) {
			issueRow.fkSeriesId = row.getInt(Columns.SERIES_ID);
		}

		issueRow.title = row.getString(Columns.TITLE);
		issueRow.instance.name = row.getString(Columns.TITLE);
		issueRow.variantName = row.getString(Columns.VARIANT_NAME);
		issueRow.volume = row.getString(Columns.VOLUME);

		if (!row.isNullAt(Columns.EDITING)) {
			CreatorField creatorField = parseField(Columns.EDITING, row,
					parserFactory.creator(issueRow.instance));
			issueRow.editors = creatorField.creators;
			issueRow.alaises = creatorField.aliases;//TODO - not doing anything with these
			
			if (creatorField.creatorAliases != null) {
				issueRow.creatorAliases = creatorField.creatorAliases;
				issueRow.creatorAliases
						.forEach(c -> c.role = CreatorRole.editor);
			}

		}

		if (!row.isNullAt(Columns.PRICE)) {
			issueRow.price = parseField(Columns.PRICE, row,
					parserFactory.price());
		}

		if (!row.isNullAt(Columns.NUMBER)) {
			issueRow.number = parseField(Columns.NUMBER, row,
					parserFactory.issueNumber());
		}

		if (!row.isNullAt(Columns.ID)) {
			issueRow.id = row.getInt(Columns.ID);
			add(issueRow);
		}
		return issueRow;

	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(IssueRow row) {
		super.transform(row);
		ComicIssue issue = row.instance;
		try {
			issue.addUrl(new URL("http://www.comics.org/issue/" + row.id));
		} catch (MalformedURLException e1) {
		}

		issue.headline = row.title;
		issue.frequency = row.indiciaFrequency;

		if (!Strings.isNullOrEmpty(row.variantName)) {
			issue.addAlternateName(row.variantName);
		}
		issue.contentRating = row.rating;
		if (row.brand != null) {
			issue.addBrand(row.brand.instanceId);
		}
		if (row.editors != null && !row.editors.isEmpty()) {
			row.editors.forEach(e -> issue.addEditor(e.instanceId));
		}

		if (row.indiciaPublisher != null) {
			issue.addPublisherImprints(row.indiciaPublisher.instanceId);
		}
		if (row.series != null) {
			issue.name = row.series.name;
			row.series.addHasPart(issue.instanceId);
			issue.addIsPartOf(row.series.instanceId);
		}

		if (!Strings.isNullOrEmpty(row.note)) {
			issue.addDescription(row.note);
		}

		if (row.onSaleDate != null) {
			issue.dateOnSale = row.onSaleDate.instanceId;
		}

		issue.datePublished = createInstant(row.keyDate, row.publicationDate);
		if (row.price != null) {
			row.price.forEach(p -> issue.addPrice(p.instanceId));
		}

		if (!Strings.isNullOrEmpty(row.volume)) {
			PublicationVolume publicationVolume = thingFactory
					.create(PublicationVolume.class);
			publicationVolume.name = row.series.name;
			publicationVolume.volumeNumber = row.volume;
			publicationVolume.addAlternateName(issue.alternateNames);
			publicationVolume.addHasPart(issue.instanceId);
			issue.addIsPartOf(publicationVolume.instanceId);
		}
		if (row.number != null) {
			issue.issueNumber = row.number.instanceId;
		}

		if (row.publisher != null) {
			issue.addPublisher(row.publisher.instanceId);
		}

		// issue.inLanguage
		// issue.locationCreated
	}

}
