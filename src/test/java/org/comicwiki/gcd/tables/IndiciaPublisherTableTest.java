package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
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


	protected IndiciaPublisherTable createTable(ThingFactory thingFactory) {
		return new IndiciaPublisherTable(null, createThingFactory());
	}
}
