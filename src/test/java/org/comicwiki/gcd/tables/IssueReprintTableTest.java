package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.IssueReprintTable.IssueReprintRow;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.model.ReprintNote;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.junit.Test;

public class IssueReprintTableTest extends TableTestCase<IssueReprintTable> {

	@Override
	protected IssueReprintTable createTable(ThingFactory thingFactory) {
		return new IssueReprintTable(null, thingFactory);
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

	@Test
	public void targetOrigin() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));

		Row issueRow = RowFactory.create(5, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "MyOriginal",
				null, null);
		issueTable.process(issueRow);
		
		Row issueRow2 = RowFactory.create(6, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "MyReprint",
				null, null);
		issueTable.process(issueRow2);
		
		IssueReprintTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, 5, 6, null);
		IssueReprintRow row2 = table.process(row);		
		table.join(new BaseTable[]{issueTable});
		
		assertNotNull(row2.original);
		assertNotNull(row2.reprint);
		
		assertEquals("MyReprint", row2.reprint.name);
		assertEquals("MyOriginal", row2.original.name);
	}
	
	@Test
	public void transformTargetOrigin() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));

		Row issueRow = RowFactory.create(5, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "MyOriginal",
				null, null);
		IssueRow ir1 = issueTable.process(issueRow);
		
		Row issueRow2 = RowFactory.create(6, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "MyReprint",
				null, null);
		IssueRow ir2 = issueTable.process(issueRow2);
		
		IssueReprintTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, 5, 6, "MyNote");
		IssueReprintRow row2 = table.process(row);		
		table.join(new BaseTable[]{issueTable});
		table.tranform();
		
		IRI iriReprint = row2.instance.reprint;
		IRI iriOriginal = row2.instance.firstPrint;
		
		ComicIssue ci1 = (ComicIssue) thingFactory.getCache().get(iriReprint);
		ComicIssue  ci2 = (ComicIssue) thingFactory.getCache().get(iriOriginal);
				
		assertTrue(row2.instance.note.contains("MyNote"));
		assertEquals("MyOriginal", ci2.name);
		assertEquals("MyReprint", ci1.name);
		
		IRI reprintNote1IRI = ir1.instance.reprintNote.iterator().next();
		IRI reprintNote2IRI = ir2.instance.reprintNote.iterator().next();
		ReprintNote rn1 = (ReprintNote) thingFactory.getCache().get(reprintNote1IRI);
		ReprintNote rn2 = (ReprintNote)thingFactory.getCache().get(reprintNote2IRI);
		
		assertEquals(rn1.firstPrint, ir1.instance.instanceId);
		assertEquals(rn1.reprint, ir2.instance.instanceId);
		
		assertEquals(rn2.firstPrint, ir1.instance.instanceId);
		assertEquals(rn2.reprint, ir2.instance.instanceId);
	}
}
