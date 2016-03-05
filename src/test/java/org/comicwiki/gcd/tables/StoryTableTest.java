/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRI;
import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.Genre;
import org.comicwiki.model.ReprintNote;
import org.comicwiki.model.StoryNote;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StoryTableTest extends TableTestCase<StoryTable> {
	@Test
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() {
		StoryTable table = createTable();
		table.saveToParquetFormat("jdb:mysql://invalid");
	}

	@Test
	public void character() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "Daredevil", null, null,
				null, null, null, null, null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Daredevil", tableRow.characters.stream().findFirst()
				.get().name);
	}

	@Test
	public void characterStoryRelation() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);
		StoryTable.StoryRow row = new StoryTable.StoryRow();

		ComicCharacter cc = thingFactory.create(ComicCharacter.class);
		row.characters = Lists.newArrayList(cc);

		table.transform(row);

		assertTrue(row.instance.characters.contains(cc.instanceId));
	}

	@Override
	protected StoryTable createTable(ThingFactory thingFactory) {
		return new StoryTable(null, thingFactory, new FieldParserFactory(
				thingFactory), new OrgLookupService());
	}

	@Test
	public void creatorWorkOnCharacter() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		StoryTable.StoryRow row = new StoryTable.StoryRow();
		ComicCharacter cc = thingFactory.create(ComicCharacter.class);
		row.characters = Lists.newArrayList(cc);

		Person inker = thingFactory.create(Person.class);
		row.inks = Lists.newArrayList(inker);
		table.transform(row);

		assertTrue(inker.workedOn.contains(cc.instanceId));
		assertTrue(cc.creativeWork.inkers.contains(inker.instanceId));
	}

	@Test
	public void description() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, "A description",
				null, null, null, null, null, null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals("A description", tableRow.synopsis);
	}

	@Test
	public void duplicateGenres() throws Exception {
		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, "superhero;western;western",
				null, null, null, null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);

		assertEquals(2, row.genre.size());
	}

	@Test
	public void genres() throws Exception {
		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, "superhero;western", null, null,
				null, null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);

		assertEquals(2, row.genre.size());
		boolean superhero = false, western = false;
		for (Genre genre : row.genre) {
			if (genre.name.equalsIgnoreCase("SUPERHERO")) {
				superhero = true;
			} else if (genre.name.equalsIgnoreCase("WESTERN")) {
				western = true;
			}
		}
		assertTrue(superhero);
		assertTrue(western);
	}

	@Test
	public void joinIndiciaPublisherTable() throws Exception {
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
		issueTable.process(issueRow);

		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null,
				3/* IssueId */, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(new BaseTable[] { pubTable, issueTable });
		assertNotNull(row2.indiciaPublisher);
		assertEquals("Fox Publications, Inc.", row2.indiciaPublisher.name);
	}

	@Test
	public void joinIssueTable() throws Exception {
		Row row = RowFactory.create(3, null, null, 5, 10, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null);
		ThingFactory thingFactory = createThingFactory();
		IssueTable table = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		table.process(row);

		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, 3, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(table);
		assertEquals(10, row2.fkIndiciaPublisherId);
		assertEquals(5, row2.fkSeriesId);
	}

	@Test
	public void joinPublisherTable() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		Row pubRow = RowFactory.create(20, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, createThingFactory());
		pubTable.process(pubRow);

		Row seriesRow = RowFactory.create(5, null, null, null, null, null,
				20 /* PublisherId */, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);

		SeriesTable seriesTable = new SeriesTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		seriesTable.process(seriesRow);

		Row issueRow = RowFactory.create(3, null, null, 5/* SeriesId */, 10,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		issueTable.process(issueRow);

		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null,
				3/* IssueId */, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(new BaseTable[] { pubTable, issueTable, seriesTable });
		assertNotNull(row2.publisher);
		assertEquals("Marvel Comics", row2.publisher.name);
	}

	@Test
	public void joinStoryTypeTable() throws Exception {
		Row row = RowFactory.create(8, "credits");
		StoryTypeTable table = new StoryTypeTable(null);
		table.process(row);

		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, 8, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(table);

		assertEquals("credits", row2.storyType);
	}

	@Test
	public void notes() throws Exception {
		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				"note1;note2", null, null, null);
		StoryRow row = storyTable.process(storyRow);

		assertEquals(2, row.notes.size());
		assertTrue(row.notes.contains("note1"));
		assertTrue(row.notes.contains("note2"));
	}

	@Test
	public void organization() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);

		OrgLookupService service = new OrgLookupService(
				Lists.newArrayList("X-Men"));

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "X-Men[Wolverine]", null,
				null, null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory,
				new FieldParserFactory(thingFactory), service);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Wolverine", tableRow.characters.stream().findFirst()
				.get().name);
		assertEquals("X-Men",
				tableRow.organizations.stream().findFirst().get().name);
	}

	@Test
	public void pageCount() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, new BigDecimal(5),
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
		StoryRow storyRow = table.process(row);
		assertEquals(5, storyRow.pageCount);
	}

	@Test
	public void pageCountCertain() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, true);
		StoryRow storyRow = table.process(row);
		assertEquals(true, storyRow.pageCountUncertain);
	}

	@Test
	public void pageCountUncertainFalseOnNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		StoryRow storyRow = table.process(row);
		assertEquals(false, storyRow.pageCountUncertain);
	}

	@Test
	public void reprintNotes() throws Exception {
		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				"reprint1; reprint2", null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);

		assertEquals(2, row.reprintNotes.size());
		assertTrue(row.reprintNotes.contains("reprint1"));
		assertTrue(row.reprintNotes.contains("reprint2"));
	}

	@Test
	public void sequenceNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 3, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(3, tableRow.sequenceNumber);
	}

	@Test
	public void title() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, "Action Comics", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);

		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals("Action Comics", tableRow.title);
	}

	@Test
	public void tranformGenres() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable storyTable = createTable(thingFactory);
		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, "superhero;western", null, null,
				null, null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);
		storyTable.tranform();
		ComicStory instance = row.instance;

		assertEquals(2, instance.genres.size());

		boolean superhero = false, western = false;
		for (IRI genre : instance.genres) {
			Genre g = (Genre) thingFactory.getCache().get(genre);
			if (g.name.equalsIgnoreCase("SUPERHERO")) {
				superhero = true;
			} else if (g.name.equalsIgnoreCase("WESTERN")) {
				western = true;
			}
		}
		assertTrue(superhero);
		assertTrue(western);
	}

	@Test
	public void tranformIndiciaPublisherTable() throws Exception {
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
		issueTable.process(issueRow);

		StoryTable storyTable = this.createTable(thingFactory);
		Row storyRow = RowFactory.create(1, null, null, null, null,
				3/* IssueId */, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(new BaseTable[] { pubTable, issueTable });
		storyTable.tranform();

		ComicStory story = row2.instance;
		assertEquals(1, story.publisherImprints.size());
		IRI publisherIri = story.publisherImprints.iterator().next();

		Organization publisher = (Organization) thingFactory.getCache().get(
				publisherIri);
		assertEquals("Fox Publications, Inc.", publisher.name);
	}

	@Test
	public void transformDescription() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, "A description",
				null, null, null, null, null, null);
		StoryTable.StoryRow tableRow = table.process(row);
		table.tranform();
		assertEquals("A description", tableRow.instance.description.iterator()
				.next());
	}

	@Test
	public void transformComicOrganizations() throws Exception {
		OrgLookupService service = new OrgLookupService(
				Lists.newArrayList("X-Men"));

		ThingFactory thingFactory = createThingFactory();
		StoryTable storyTable = new StoryTable(null, thingFactory,
				new FieldParserFactory(thingFactory), service);

		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "X-Men[Wolverine]", null,
				null, null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);
		storyTable.tranform();
		ComicStory story = row.instance;

		assertEquals(1, story.fictionalOrganizations.size());

		Organization organization = (Organization) thingFactory.getCache().get(
				story.fictionalOrganizations.iterator().next());
		assertEquals("X-Men", organization.name);
	}

	@Test
	public void transformNotes() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		StoryTable storyTable = this.createTable(thingFactory);

		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				"note1;note2", null, null, null);
		StoryRow row = storyTable.process(storyRow);
		storyTable.tranform();
		ComicStory story = row.instance;

		assertEquals(1, story.storyNote.size());

		StoryNote storyNote = (StoryNote) thingFactory.getCache().get(
				story.storyNote.iterator().next());
		assertTrue(storyNote.note.contains("note1"));
		assertTrue(storyNote.note.contains("note2"));
	}

	@Test
	public void transformPublisherTable() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		Row pubRow = RowFactory.create(20, "Marvel Comics", null, null, null,
				null, null, null);
		PublisherTable pubTable = new PublisherTable(null, thingFactory);
		pubTable.process(pubRow);

		Row seriesRow = RowFactory.create(5, null, null, null, null, null,
				20 /* PublisherId */, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);
		SeriesTable seriesTable = new SeriesTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		seriesTable.process(seriesRow);

		Row issueRow = RowFactory.create(3, null, null, 5/* SeriesId */, 10,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		IssueTable issueTable = new IssueTable(null, thingFactory,
				new FieldParserFactory(thingFactory));
		issueTable.process(issueRow);

		StoryTable storyTable = createTable(thingFactory);
		Row storyRow = RowFactory.create(1, null, null, null, null,
				3/* IssueId */, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		StoryRow row2 = storyTable.process(storyRow);
		storyTable.join(new BaseTable[] { pubTable, issueTable, seriesTable });
		storyTable.tranform();

		ComicStory story = row2.instance;
		assertEquals(1, story.publishers.size());
		IRI publisherIri = story.publishers.iterator().next();

		Organization publisher = (Organization) thingFactory.getCache().get(
				publisherIri);
		assertEquals("Marvel Comics", publisher.name);
	}

	@Test
	public void transformReprintNotes() throws Exception {

		ThingFactory thingFactory = createThingFactory();
		StoryTable storyTable = this.createTable(thingFactory);

		Row storyRow = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "note1;note2",
				null, null, null, null, null);
		StoryRow row = storyTable.process(storyRow);
		storyTable.tranform();
		ComicStory story = row.instance;

		assertEquals(1, story.reprintNote.size());

		ReprintNote reprintNote = (ReprintNote) thingFactory.getCache().get(
				story.reprintNote.iterator().next());
		assertTrue(reprintNote.note.contains("note1"));
		assertTrue(reprintNote.note.contains("note2"));
	}

	@Test
	public void transformSequenceNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(1, null, null, 3, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null);
		StoryTable.StoryRow tableRow = table.process(row);
		table.tranform();
		assertEquals(3, tableRow.instance.position);
	}
}
