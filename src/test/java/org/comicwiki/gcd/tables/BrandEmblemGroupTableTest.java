package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.BrandEmblemGroupTable.BrandEmblemGroupRow;
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
		assertEquals(0, table.rowCache.size());
	}
	
	@Test
	public void id() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		BrandEmblemGroupTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, 2, 3);
		BrandEmblemGroupRow row2 = table.process(row);
		assertEquals(1, table.rowCache.size());
		
		assertEquals(new Integer(2), row2.brandId);
		assertEquals(new Integer(3), row2.brandGroupId);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() throws Exception {
		super.badJcbcUrl();
	}

}
