package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public class CountryTableTest extends TableTestCase<CountryTable> {

	protected CountryTable createTable(ThingFactory thingFactory) {
		return new CountryTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void countryCode() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, "US", null);
		table.process(row);
		assertEquals(1, table.cache.size());
		assertEquals("US", table.cache.get(1).instance.countryCode.iterator()
				.next());

	}
	
	@Test
	public void countryName() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		CountryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, "United States");
		table.process(row);
		assertEquals(1, table.cache.size());
		assertEquals("United States", table.cache.get(1).instance.name);

	}
}
