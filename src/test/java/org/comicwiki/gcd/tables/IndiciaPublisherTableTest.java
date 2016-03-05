package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.IndiciaPublisherTable.IndiciaPublisherRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.model.schema.Organization;
import org.junit.Test;

public class IndiciaPublisherTableTest extends
		TableTestCase<IndiciaPublisherTable> {

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IndiciaPublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
	
	@Test
	public void transform() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IndiciaPublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null);
		table.process(row);
		table.tranform();
		assertEquals(1, table.cache.size());
	}
	
	@Test
	public void country() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable countryTable = new CountryTable(null, thingFactory);
		Row countryRow = RowFactory.create(225, "us", "United States");
		countryTable.process(countryRow);

		IndiciaPublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, 225, null, null, null, null,
				null, null);
		IndiciaPublisherRow row2 = table.process(row);
		table.join(new BaseTable[]{countryTable});
		table.tranform();

		assertNotNull(row2.country);
		assertEquals("United States", row2.country.name);
		assertEquals("us", row2.country.countryCode.iterator().next());
	}

	@Test
	public void transformPublisherTable() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		Row pubRow = RowFactory.create(20, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, thingFactory);
		pubTable.process(pubRow);

		IndiciaPublisherTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, 20);

		IndiciaPublisherRow row2 = table.process(row);
		table.join(new BaseTable[] { pubTable });
		table.tranform();

		assertNotNull(row2.instance.parentOrganization);

		IRI publisherIri = row2.instance.parentOrganization;
		Organization publisher = (Organization) thingFactory.getCache().get(
				publisherIri);

		assertEquals("Marvel Comics", publisher.name);
	}


	protected IndiciaPublisherTable createTable(ThingFactory thingFactory) {
		return new IndiciaPublisherTable(null, createThingFactory());
	}
}
