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
package org.comicwiki.gcdb.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.junit.Test;

public class StoryTableTest {

	/**
	 * new Column("id"), new Column("title"), new Column("feature"), new
	 * Column("sequence_number"), new Column("page_count"), new
	 * Column("issue_id"), new Column("script"), new Column("pencils"), new
	 * Column("inks"), new Column("colors"), new Column("letters"), new
	 * Column("editing"), new Column("genre"), new Column("characters"), new
	 * Column("synopsis"), new Column("reprint_notes"), new Column("modified"),
	 * new Column("notes"), new Column("type_id"), new Column("job_number"), new
	 * Column("page_count_uncertain") };
	 */
	@Test
	public void allNull() throws Exception {
		Row row = RowFactory.create(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		StoryTable table = new StoryTable(null);
		table.process(row);
		assertEquals(0, table.cache.size());
	}

	@Test
	public void character() throws Exception {
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "Daredevil", null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Daredevil", tableRow.characters.stream().findFirst()
				.get().name);
	}

	@Test
	public void organization() throws Exception {
		Row row = RowFactory.create(1, null, null, null, null, null, null,
				null, null, null, null, null, null, "X-Men[Wolverine]", null,
				null, null, null, null, null, null);
		StoryTable table = new StoryTable(null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals(1, tableRow.characters.size());
		assertEquals("Wolverine", tableRow.characters.stream().findFirst()
				.get().name);
		assertEquals("X-Men",
				tableRow.organizations.stream().findFirst().get().name);
	}

	@Test
	public void title() throws Exception {
		Row row = RowFactory.create(1, "Action Comics", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		StoryTable table = new StoryTable(null);
		StoryTable.StoryRow tableRow = table.process(row);
		assertEquals("Action Comics", tableRow.title);
	}
}
