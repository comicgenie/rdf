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

import static org.junit.Assert.*;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.schema.Person;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoryTableTest {

	
	@Test
	public void characterStoryRelation() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
		StoryTable.StoryRow row = new StoryTable.StoryRow();
		ComicCharacter cc = thingFactory.create(ComicCharacter.class);
		row.characters = Lists.newArrayList(cc);
		
		table.transform(row);
		
		assertTrue(row.instance.characters.contains(cc.instanceId));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
		table.saveToParquetFormat("dsafjkldfksa");
	}
	
	@Test(expected = NullPointerException.class)
	public void badJcbcUrl2() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
		table.saveToParquetFormat("jdbc:mysql://invalid");
	}
	
	@Test
	public void creatorWorkOnCharacter() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
		StoryTable.StoryRow row = new StoryTable.StoryRow();
		ComicCharacter cc = thingFactory.create(ComicCharacter.class);
		row.characters = Lists.newArrayList(cc);
		
		Person inker = thingFactory.create(Person.class);
		row.inks = Lists.newArrayList(inker);
		table.transform(row);
		System.out.println(row.inks);
		
		assertTrue(inker.workedOn.contains(cc.instanceId));
		assertTrue(cc.creativeWork.inkers.contains(inker.instanceId));
	}
	
	@Test
	public void allNull() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void character() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "Daredevil", null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
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
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);

		Row row = RowFactory.create(1, "Action Comics", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, new IRICache(),
				null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals("Action Comics", tableRow.title);
	}
}
