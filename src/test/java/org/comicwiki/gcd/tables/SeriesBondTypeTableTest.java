package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public class SeriesBondTypeTableTest extends TableTestCase<SeriesBondTypeTable>{

	@Override
	protected SeriesBondTypeTable createTable(ThingFactory thingFactory) {
		return new SeriesBondTypeTable(null);
	}
	
	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesBondTypeTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null);
		table.process(row);
		assertEquals(0, table.rowCache.size());
	}


}
