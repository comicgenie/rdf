package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.PublisherTable.PublisherRow;
import org.comicwiki.model.Instant;
import org.junit.Test;

public class PublisherTableTest extends TableTestCase<PublisherTable> {

	@Override
	protected PublisherTable createTable(ThingFactory thingFactory) {
		return new PublisherTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		PublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
	
	@Test
	public void country() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable countryTable = new CountryTable(null, thingFactory);
		Row countryRow = RowFactory.create(225, "us", "United States");
		countryTable.process(countryRow);

		PublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, 225, null, null, null, null,
				null, null);
		PublisherRow row2 = table.process(row);
		table.join(new BaseTable[]{countryTable});
		table.tranform();

		assertNotNull(row2.instance.location);
		assertEquals("United States", row2.country.name);
		assertEquals("us", row2.country.countryCode.iterator().next());
	}
	
	@Test
	public void yearBegin() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		PublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 1940, null, null, null,
				null, null);
		PublisherRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(1940), row2.yearBegan);

		Instant begin = (Instant) thingFactory.getCache().get(
				row2.instance.foundingDate);
		assertEquals(1940, begin.year);
	}

	@Test
	public void yearEnd() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		PublisherTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, 2016, null, null,
				null, null);
		PublisherRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(2016), row2.yearEnded);

		Instant end = (Instant) thingFactory.getCache().get(
				row2.instance.dissolutionDate);
		assertEquals(2016, end.year);
	}

}
