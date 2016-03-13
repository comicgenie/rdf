package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public class SeriesPublicationTypeTableTest extends TableTestCase<SeriesPublicationTypeTable>{

	@Override
	protected SeriesPublicationTypeTable createTable(ThingFactory thingFactory) {
		return new SeriesPublicationTypeTable(null);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		SeriesPublicationTypeTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null);
		table.process(row);
		assertEquals(0, table.rowCache.size());
	}
}
