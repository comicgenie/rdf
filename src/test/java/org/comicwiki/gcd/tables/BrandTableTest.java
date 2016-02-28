package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public class BrandTableTest extends TableTestCase<BrandTable>{

	@Override
	protected BrandTable createTable(ThingFactory thingFactory) {
		return new BrandTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null,
				null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
}
