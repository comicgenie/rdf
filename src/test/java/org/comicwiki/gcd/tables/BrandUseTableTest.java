package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.BrandUseTable.BrandUseRow;
import org.comicwiki.model.Instant;
import org.junit.Test;

public class BrandUseTableTest extends TableTestCase<BrandUseTable> {

	@Override
	protected BrandUseTable createTable(ThingFactory thingFactory) {
		return new BrandUseTable(null, thingFactory);
	}
	
	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandUseTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null);
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
		BrandUseTable table = createTable(thingFactory);
		
		Row row = RowFactory.create(1, null, null, null, 2016, null, null);
		BrandUseRow row2 = table.process(row);
		table.tranform();
		
		assertEquals(new Integer(2016), row2.yearEnded);
		
		Instant end = (Instant) thingFactory.getCache().get(row2.instance.end);
		assertEquals(2016, end.year);
	//	assertNotNull(end.name);
	}
	
	@Test
	public void yearBegin() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		BrandUseTable table = createTable(thingFactory);
		
		Row row = RowFactory.create(1, null, null, 1940, null, null, null);
		BrandUseRow row2 = table.process(row);
		table.tranform();
		
		assertEquals(new Integer(1940), row2.yearBegan);
		
		Instant begin = (Instant) thingFactory.getCache().get(row2.instance.begin);
		assertEquals(1940, begin.year);
	}
	
	@Test
	public void notes() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandUseTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, "My note", null);
		BrandUseRow row2 = table.process(row);
		table.tranform();
		assertEquals("My note", row2.notes);
		assertTrue(row2.instance.description.contains("My note"));
	}

}
