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

import java.io.File;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.CharacterCreator;
import org.comicwiki.gcd.tables.StoryTable;
import org.junit.Test;

public class StoryTableTest {

	private File resourceDir = new File("./src/main/resources/comics");

	@Test
	public void allNull() throws Exception {
		ThingCache thingCache = new ThingCache();
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, characterCreator,
				resourceDir);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void character() throws Exception {
		ThingCache thingCache = new ThingCache();
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "Daredevil", null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, characterCreator,
				resourceDir);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Daredevil", tableRow.characters.stream().findFirst()
				.get().name);
	}

	@Test
	public void organization() throws Exception {
		ThingCache thingCache = new ThingCache();
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "X-Men[Wolverine]", null,
				null, null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, characterCreator,
				resourceDir);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Wolverine", tableRow.characters.stream().findFirst()
				.get().name);
		assertEquals("X-Men",
				tableRow.organizations.stream().findFirst().get().name);
	}

	@Test
	public void title() throws Exception {
		ThingCache thingCache = new ThingCache();
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		Row row = RowFactory.create(1, "Action Comics", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null, thingFactory, characterCreator,
				resourceDir);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals("Action Comics", tableRow.title);
	}
}
