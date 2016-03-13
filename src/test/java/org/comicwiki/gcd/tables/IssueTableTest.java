package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.fields.PriceFieldParser;
import org.comicwiki.gcd.tables.BrandTable.BrandRow;
import org.comicwiki.gcd.tables.IssueReprintTable.IssueReprintRow;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.model.notes.ReprintNote;
import org.comicwiki.model.prices.DecimalPrice;
import org.comicwiki.model.prices.Price;
import org.comicwiki.model.schema.Brand;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.PublicationVolume;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.junit.Test;

import com.google.common.collect.Lists;

public class IssueTableTest extends TableTestCase<IssueTable> {

	@Override
	protected IssueTable createTable(ThingFactory thingFactory) {
		return new IssueTable(null, thingFactory, new FieldParserFactory(
				thingFactory));
	}

	@Test
	public void brand() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		FieldParserFactory factory = new FieldParserFactory(thingFactory);

		BrandTable brandTable = new BrandTable(null, thingFactory);

		Row row = RowFactory.create(10, "Marvel", null, null, null, null, null);
		BrandRow brandRow = brandTable.process(row);

		IssueTable table = new IssueTable(null, thingFactory, factory);

		Row row2 = RowFactory.create(1, null, null, null, null, 10, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null);
		IssueRow issueRow = table.process(row2);
		table.joinTables(new BaseTable[] { brandTable });
		table.tranform();
		assertEquals(1, Arrays.asList(issueRow.instance.brands).size());

		Brand brand = (Brand) thingFactory.getCache().get(
				issueRow.instance.brands[0]);
		assertEquals("Marvel", brand.name);
	}

	@Test
	public void price() throws Exception {
		ThingFactory thingFactory = createThingFactory();

		DecimalPrice p1 = new DecimalPrice();
		p1.amount = 1;
		p1.currency = "USD";
		PriceFieldParser fieldParser = mock(PriceFieldParser.class);
		when(fieldParser.parse(anyInt(), anyObject())).thenReturn(
				Lists.newArrayList(p1));

		FieldParserFactory factory = mock(FieldParserFactory.class);
		when(factory.price()).thenReturn(fieldParser);

		IssueTable table = new IssueTable(null, thingFactory, factory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, "40.00 FRF; 5.0 BEF; 0.50 CHF", null, null, null, null,
				null, null, null, null, null, null, null);
		IssueRow issueRow = table.process(row);
		assertEquals(1, table.rowCache.size());

		assertTrue(issueRow.price.contains(p1));
	}

	@Test
	public void tranformPrice() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, "40.00 FRF; 5.0 BEF; 0.50 CHF", null, null, null, null,
				null, null, null, null, null, null, null);
		IssueRow issueRow = table.process(row);
		table.tranform();
	}
	
	
	@Test
	public void transformPublisher() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		
		Row pubRow = RowFactory.create(4, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, thingFactory);
		pubTable.process(pubRow);
			
		Row seriesRow = RowFactory.create(5, null, null, null, null, null,
				4 /* PublisherId */, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);

		SeriesTable seriesTable = new SeriesTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		seriesTable.process(seriesRow);
		
		IssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 5, null, null, null,
				null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		IssueRow issueRow = table.process(row);		
		table.joinTables(new BaseTable[] { pubTable, seriesTable });
		table.tranform();
		
		assertNotNull(issueRow.publisher);
		assertEquals("Marvel Comics", issueRow.publisher.name);
	}

	@Test
	public void transformIndiciaPublisher() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IndiciaPublisherTable pubTable = new IndiciaPublisherTable(null,
				thingFactory);

		Row row = RowFactory.create(4, "Fox Publications, Inc.", null, null,
				null, null, null, null, null);
		pubTable.process(row);

		Row issueRow = RowFactory.create(3, null, null, null,
				4/* IndiciaPublisherId */, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null);

		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		IssueRow row2 = issueTable.process(issueRow);
				
		issueTable.joinTables(new BaseTable[] { pubTable });
		issueTable.tranform();
		
		IRI iri = row2.instance.publisherImprints.iterator().next();
		Organization org = (Organization) thingFactory.getCache().get(iri);
		
		assertNotNull(org);
		assertEquals("Fox Publications, Inc.", org.name);
	}

	@Test
	public void volume() throws Exception {
		ThingFactory thingFactory = createThingFactory();

		Row seriesRow = RowFactory.create(5, "Amazing Spiderman", null, null,
				null, null, 20 /* PublisherId */, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null);

		SeriesTable seriesTable = new SeriesTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		seriesTable.process(seriesRow);

		IssueTable table = createTable(thingFactory);
		Row row = RowFactory.create(1, null, "3", 5, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null);
		IssueRow issueRow = table.process(row);
		table.joinTables(new BaseTable[] { seriesTable });

		assertNotNull(issueRow.series);
		assertNotNull(issueRow.series.name);
		assertEquals("Amazing Spiderman", issueRow.series.name);
	}

	@Test
	public void tranformVolume() throws Exception {
		ThingFactory thingFactory = createThingFactory();

		Row seriesRow = RowFactory.create(5, "Amazing Spiderman", null, null,
				null, null, 20 /* PublisherId */, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null);

		SeriesTable seriesTable = new SeriesTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		seriesTable.process(seriesRow);

		IssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, "3", 5, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null);
		IssueRow issueRow = table.process(row);
		table.joinTables(new BaseTable[] { seriesTable });
		table.tranform();

		Iterator<IRI> it = issueRow.instance.isPartOf.iterator();
		IRI iri = null;
		while(it.hasNext()) {
			Thing thing = thingFactory.getCache().get(
					it.next());
			if(thing instanceof PublicationVolume) {
				iri = thing.instanceId;
			}		
		}

		PublicationVolume pv = (PublicationVolume) thingFactory.getCache().get(
				iri);
		assertEquals("3", pv.volumeNumber);
		assertEquals("Amazing Spiderman", pv.name);
	}

	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		table.process(row);
		assertEquals(0, table.rowCache.size());
	}

}
