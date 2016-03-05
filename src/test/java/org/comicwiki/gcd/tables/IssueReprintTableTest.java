package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.IssueReprintTable.IssueReprintRow;
import org.junit.Test;

public class IssueReprintTableTest extends TableTestCase<IssueReprintTable>{

	@Override
	protected IssueReprintTable createTable(ThingFactory thingFactory) {
		return new IssueReprintTable(null);
	}
	
	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueReprintTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}
	
	@Test
	public void ids() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueReprintTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, 2, 3, null);
		IssueReprintRow row2 = table.process(row);
		assertEquals(1, table.cache.size());
		assertEquals(2, row2.fkOriginIssueId);
		assertEquals(3, row2.fkTargetIssueId);
	}
	
	@Test
	public void notes() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueReprintTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, "My note");
		IssueReprintRow row2 = table.process(row);
		assertEquals(1, table.cache.size());
		assertEquals("My note", row2.notes);
	}

}
