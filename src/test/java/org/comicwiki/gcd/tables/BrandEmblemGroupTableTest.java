package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public class BrandEmblemGroupTableTest extends
		TableTestCase<BrandEmblemGroupTable> {

	@Override
	protected BrandEmblemGroupTable createTable(ThingFactory thingFactory) {
		return new BrandEmblemGroupTable(null);
	}
	
	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandEmblemGroupTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

}
