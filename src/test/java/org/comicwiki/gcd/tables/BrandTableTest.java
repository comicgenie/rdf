package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.BrandTable.BrandRow;
import org.comicwiki.model.Instant;
import org.junit.Test;

public class BrandTableTest extends TableTestCase<BrandTable> {

	@Override
	protected BrandTable createTable(ThingFactory thingFactory) {
		return new BrandTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void yearEnd() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		BrandTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 2016, null, null, null);
		BrandRow row2 = table.process(row);
		table.tranform();

		assertEquals(2016, row2.yearEnded);

		Instant end = (Instant) thingFactory.getCache().get(
				row2.instance.endUseDate);
		assertEquals(2016, end.year);
	}

	@Test
	public void yearBegin() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		BrandTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, 1940, null, null, null, null);
		BrandRow row2 = table.process(row);
		table.tranform();

		assertEquals(1940, row2.yearBegan);

		Instant begin = (Instant) thingFactory.getCache().get(
				row2.instance.startUseDate);
		assertEquals(1940, begin.year);
	}

	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() throws Exception {
		super.badJcbcUrl();
	}
}
