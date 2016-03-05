package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.BrandGroupTable.BrandGroupRow;
import org.comicwiki.model.Instant;
import org.junit.Test;

public class BrandGroupTableTest extends TableTestCase<BrandGroupTable> {

	@Override
	protected BrandGroupTable createTable(ThingFactory thingFactory) {
		return new BrandGroupTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandGroupTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() throws Exception {
		super.badJcbcUrl();
	}
	
	@Test
	public void yearEnd() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		BrandGroupTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 2016, null, null, null, null);
		BrandGroupRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(2016), row2.yearEnded);

		Instant end = (Instant) thingFactory.getCache().get(
				row2.instance.endUseDate);
		assertEquals(2016, end.year);
	}

	@Test
	public void yearBegin() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		BrandGroupTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, 1940, null, null, null, null, null);
		BrandGroupRow row2 = table.process(row);
		table.tranform();

		assertEquals(new Integer(1940), row2.yearBegan);

		Instant begin = (Instant) thingFactory.getCache().get(
				row2.instance.startUseDate);
		assertEquals(1940, begin.year);
	}

}
