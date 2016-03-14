package org.comicwiki.gcd.tables;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.OrgLookupService;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.IssueReprintTable.IssueReprintRow;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.ReprintFromIssueTable.ReprintFromIssueRow;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.junit.Test;

public class ReprintFromIssueTableTest extends
		TableTestCase<ReprintFromIssueTable> {

	@Override
	protected ReprintFromIssueTable createTable(ThingFactory thingFactory) {
		return new ReprintFromIssueTable(null, thingFactory);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ReprintFromIssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null);
		table.process(row);
		assertEquals(0, table.rowCache.size());
	}

	@Test
	public void ids() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ReprintFromIssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, 2, 3, null);
		ReprintFromIssueRow row2 = table.process(row);
		assertEquals(1, table.rowCache.size());
		assertEquals(2, row2.fkOriginIssueId);
		assertEquals(3, row2.fkTargetStoryId);
	}

	@Test
	public void notes() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ReprintFromIssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, "My note");
		ReprintFromIssueRow row2 = table.process(row);
		assertEquals(1, table.rowCache.size());
		assertEquals("My note", row2.notes);
	}

	@Test
	public void targetOrigin() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));

		Row issueRow = RowFactory.create(5, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				"MyOriginal", null, null);
		issueTable.process(issueRow);

		StoryTable storyTable = new StoryTable(null, thingFactory,
				new FieldParserFactory(thingFactory), new OrgLookupService());

		Row row = RowFactory.create(6, null, null, null, null, 5, null, null,
				null, null, null, null, null, "Daredevil", null, null, null,
				null, null, null, null);
		StoryRow tableRow = storyTable.process(row);

		ReprintFromIssueTable reprintTable = createTable(thingFactory);
		Row reprintRow = RowFactory.create(1, 5, 6, null);
		ReprintFromIssueRow reprintFromRow = reprintTable.process(reprintRow);
		reprintTable.joinTables(new BaseTable[] { issueTable, storyTable });
		// reprintTable.tranform();
		assertNotNull(reprintFromRow.originalIssue);
		assertNotNull(reprintFromRow.reprintStory);
	}

	@Test
	public void tranformTargetOrigin() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));

		Row issueRow = RowFactory.create(5, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				"MyOriginal", null, null);
		IssueRow ir1 = issueTable.process(issueRow);

		StoryTable storyTable = new StoryTable(null, thingFactory,
				new FieldParserFactory(thingFactory), new OrgLookupService());

		Row row = RowFactory.create(6, null, null, null, null, 5, null, null,
				null, null, null, null, null, "Daredevil", null, null, null,
				null, null, null, null);
		StoryRow tableRow = storyTable.process(row);

		ReprintFromIssueTable reprintTable = createTable(thingFactory);
		Row reprintRow = RowFactory.create(1, 5, 6, null);

		ReprintFromIssueRow reprintFromRow = reprintTable.process(reprintRow);
		reprintTable.joinTables(new BaseTable[] { issueTable, storyTable });
		reprintTable.tranform();

		assertEquals(reprintFromRow.instance.firstPrint,
				ir1.instance.instanceId);
		assertEquals(reprintFromRow.instance.reprint,
				tableRow.instance.instanceId);

		assertTrue(Arrays.asList(reprintFromRow.originalIssue.reprintNote)
				.contains(reprintFromRow.instance.instanceId));
	}

}
