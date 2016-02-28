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
import static org.junit.Assert.assertTrue;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.BaseTable;
import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.schema.Person;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StoryTableTest extends TableTestCase<StoryTable> {
	
	@Test
	public void joinStoryTypeTable() throws Exception {
		Row row = RowFactory.create(8, "credits");
		StoryTypeTable table = new StoryTypeTable(null);
		table.process(row);
		
		StoryTable storyTable = createTable();
		Row storyRow = RowFactory.create(1, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, 8, null, null);
		StoryRow row2 = storyTable.process(storyRow);	
		storyTable.join(table);
		
		assertEquals("credits", row2.storyType);
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
	public void allNull() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		StoryTable table = createTable(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		table.process(row);
		assertEquals(0, table.cache.size());
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
	public void organization() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);

		OrgLookupService service = 
				new OrgLookupService(Lists.newArrayList("X-Men"));
		
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "X-Men[Wolverine]", null,
				null, null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				service);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Wolverine", tableRow.characters.stream().findFirst()
				.get().name);
		assertEquals("X-Men",
				tableRow.organizations.stream().findFirst().get().name);
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

	@Override
	protected StoryTable createTable(ThingFactory thingFactory) {
		return new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
	}
}
