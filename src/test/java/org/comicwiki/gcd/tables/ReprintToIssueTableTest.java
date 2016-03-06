package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.ReprintTable.ReprintRow;
import org.comicwiki.gcd.tables.ReprintToIssueTable.ReprintToIssueRow;
import org.junit.Test;

public class ReprintToIssueTableTest extends TableTestCase<ReprintToIssueTable>{

	@Override
	protected ReprintToIssueTable createTable(ThingFactory thingFactory) {
		return new ReprintToIssueTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ReprintToIssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
	
	@Test
	public void notes() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ReprintToIssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, "My note");
		ReprintToIssueRow row2 = table.process(row);
		assertEquals(1, table.cache.size());
		assertEquals("My note", row2.notes);
	}

}
