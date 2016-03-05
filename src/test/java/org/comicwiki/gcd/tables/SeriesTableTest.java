package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.fields.PublishDateFieldParser;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.model.Instant;
import org.comicwiki.model.TemporalEntity;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Language;
import org.comicwiki.model.schema.Organization;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

public class SeriesTableTest extends TableTestCase<SeriesTable> {

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void publicationDates() throws Exception {
		ThingFactory thingFactory = createThingFactory();

		PublishDateFieldParser fieldParser = mock(PublishDateFieldParser.class);
		TemporalEntity result = thingFactory.create(TemporalEntity.class);
		when(fieldParser.parse(anyInt(), anyObject())).thenReturn(result);

		FieldParserFactory factory = mock(FieldParserFactory.class);
		when(factory.publishDate()).thenReturn(fieldParser);

		SeriesTable table = new SeriesTable(null, thingFactory, factory);

		Row row = RowFactory.create(1, null, null, null, null, "March 2014",
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
		SeriesRow seriesRow = table.process(row);
		assertEquals(1, table.cache.size());
		assertTrue(seriesRow.publicationDates.equals(result));
		;
	}

	@Test
	public void binding() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				"squarebound (#1-14); saddle-stitched (#15-46)", null, null,
				null, null);
		SeriesRow seriesRow = table.process(row);
		assertEquals(1, table.cache.size());
		assertTrue(seriesRow.binding.contains("squarebound (#1-14)"));
		assertTrue(seriesRow.binding.contains("saddle-stitched (#15-46)"));
	}

	@Test
	public void country() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable countryTable = new CountryTable(null, thingFactory);
		Row countryRow = RowFactory.create(225, "us", "United States");
		countryTable.process(countryRow);

		SeriesTable seriesTable = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null, 225/* countryId */
		, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		SeriesRow seriesRow = seriesTable.process(row);
		seriesTable.join(countryTable);

		assertNotNull(seriesRow.country);
		assertEquals("United States", seriesRow.country.name);
		assertEquals("us", seriesRow.country.countryCode.iterator().next());
	}

	@Override
	protected SeriesTable createTable(ThingFactory thingFactory) {
		return new SeriesTable(null, thingFactory, new FieldParserFactory(
				thingFactory));
	}

	@Test
	public void dimensions() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null,
				"Standard Modern Age US", null, null, null, null, null, null,
				null);
		SeriesRow seriesRow = table.process(row);
		assertEquals(1, table.cache.size());
		assertTrue(seriesRow.dimensions.contains("Standard Modern Age US"));
	}

	@Test
	public void joinPublisherTable() throws Exception {
		Row pubRow = RowFactory.create(20, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, createThingFactory());
		pubTable.process(pubRow);

		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, 20, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);

		SeriesRow seriesRow = table.process(row);
		table.join(new BaseTable[] { pubTable });

		assertNotNull(seriesRow.publisher);
		assertEquals("Marvel Comics", seriesRow.publisher.name);
	}

	@Test
	public void language() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		LanguageTable languageTable = new LanguageTable(null, thingFactory);
		Row languageRow = RowFactory.create(3, "en", "English");
		languageTable.process(languageRow);

		SeriesTable seriesTable = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, 3/** languageId */
				, null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		SeriesRow seriesRow = seriesTable.process(row);
		seriesTable.join(languageTable);

		assertNotNull(seriesRow.language);
		assertEquals("English", seriesRow.language.name);
		assertEquals("en", seriesRow.language.languageCode);
	}

	@Test
	public void stock() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				"Glossy cover; Newsprint interior", null, null, null, null,
				null);
		SeriesRow seriesRow = table.process(row);
		assertEquals(1, table.cache.size());
		assertTrue(seriesRow.paperStock.contains("Glossy cover"));
		assertTrue(seriesRow.paperStock.contains("Newsprint interior"));
	}

	@Test
	public void transformCountry() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable countryTable = new CountryTable(null, thingFactory);
		Row countryRow = RowFactory.create(225, "us", "United States");
		countryTable.process(countryRow);

		SeriesTable seriesTable = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null, 225/**
		 * 
		 * 
		 * 
		 * countryId
		 */
		, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		SeriesRow seriesRow = seriesTable.process(row);
		seriesTable.join(countryTable);
		seriesTable.tranform();

		assertNotNull(seriesRow.instance.locationCreated);
		Country country = (Country) thingFactory.getCache().get(
				seriesRow.instance.locationCreated);

		assertEquals("United States", country.name);
		assertEquals("us", country.countryCode.iterator().next());
	}

	@Test
	public void transformDimensions() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null,
				"Standard Modern Age US", null, null, null, null, null, null,
				null);
		SeriesRow seriesRow = table.process(row);
		table.tranform();

		assertTrue(seriesRow.instance.dimensions
				.contains("Standard Modern Age US"));
	}

	@Test
	public void transformColor() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null,
				"color cover; black & white interior", null, null, null, null,
				null, null, null, null);
		SeriesRow seriesRow = table.process(row);
		table.tranform();

		assertTrue(seriesRow.instance.colors.contains("color cover"));
		assertTrue(seriesRow.instance.colors.contains("black & white interior"));
	}

	// hardcover; 222x158mm; farger
	@Test
	public void transformFormatToBinding() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, "hardcover; 222x158mm; farger",
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null);
		SeriesRow seriesRow = table.process(row);
		table.tranform();

		assertTrue(seriesRow.instance.binding.contains("hardcover"));
	}

	@Test
	public void transformPublishingFormat() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, "Limited Series", null, null, null);
		SeriesRow seriesRow = table.process(row);
		table.tranform();

		assertTrue(seriesRow.instance.format.contains("Limited Series"));
	}

	@Test
	public void transformLanguage() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		LanguageTable languageTable = new LanguageTable(null, thingFactory);
		Row languageRow = RowFactory.create(3, "en", "English");
		languageTable.process(languageRow);

		SeriesTable seriesTable = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, 3/** languageId */
				, null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		SeriesRow seriesRow = seriesTable.process(row);
		seriesTable.join(languageTable);
		seriesTable.tranform();

		assertNotNull(seriesRow.instance.inLanguage);

		Language language = (Language) thingFactory.getCache().get(
				seriesRow.instance.inLanguage);

		assertEquals("English", language.name);
		assertEquals("en", language.languageCode);
	}

	@Test
	public void transformPublisherTable() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		Row pubRow = RowFactory.create(20, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, thingFactory);
		pubTable.process(pubRow);

		SeriesTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, 20, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);

		SeriesRow seriesRow = table.process(row);
		table.join(new BaseTable[] { pubTable });
		table.tranform();

		assertNotNull(seriesRow.instance.publishers);

		IRI publisherIri = seriesRow.instance.publishers.iterator().next();
		Organization publisher = (Organization) thingFactory.getCache().get(
				publisherIri);

		assertEquals("Marvel Comics", publisher.name);
	}

	@Test
	public void yearBegin() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 1940, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		SeriesRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(1940), row2.yearBegan);

		Instant begin = (Instant) thingFactory.getCache().get(
				row2.instance.startDate);
		assertEquals(1940, begin.year);
	}

	@Test
	public void yearEnd() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		SeriesTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, 2016, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		SeriesRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(2016), row2.yearEnded);

		Instant end = (Instant) thingFactory.getCache().get(
				row2.instance.endDate);
		assertEquals(2016, end.year);
	}

}
