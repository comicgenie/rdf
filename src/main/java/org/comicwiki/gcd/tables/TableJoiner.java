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

import java.util.stream.Stream;

public class TableJoiner {

	public void join(IssueTable issueTable, StoryTable storyTable) {
		for (IssueTable.IssueRow issuesRow : issueTable.cache.values()) {
			Stream<StoryTable.StoryRow> storyRows = storyTable.cache.values()
					.stream().filter(r -> r.issueId == issuesRow.id);
			storyRows.forEach(storyRow -> {
				// do intertable relationship matching
				});

		}
	}

	public void join(SeriesTable seriesTable, IssueTable issueTable) {
		for (SeriesTable.SeriesRow seriesRow : seriesTable.cache.values()) {
			Stream<IssueTable.IssueRow> storyRows = issueTable.cache.values()
					.stream().filter(r -> r.seriesId == seriesRow.id);
			storyRows.forEach(story -> {

			});
		}
	}

	/**
	 * public static final class StoryRow extends TableRow {
	 * 
	 * public ComicStory comicStory;
	 * 
	 * public Collection<ComicCharacter> characters;
	 * 
	 * public Collection<Person> colors;
	 * 
	 * public Collection<Person> editing;
	 * 
	 * public String feature;
	 * 
	 * public Collection<String> genre;
	 * 
	 * public int id;
	 * 
	 * public Collection<Person> inks;
	 * 
	 * @Join(field = "issue_id", table = IssueTable.class) public int issueId;
	 * 
	 *             public String jobNumber;
	 * 
	 *             public Collection<Person> letters;
	 * 
	 *             public Date modified;
	 * 
	 *             public Collection<String> notes;
	 * 
	 *             public Collection<ComicOrganization> organizations;
	 * 
	 *             public int pageCount;
	 * 
	 *             public Collection<Person> pencils;
	 * 
	 *             public Collection<String> reprintNotes;;
	 * 
	 *             public Collection<Person> script;
	 * 
	 *             public int sequenceNumber;;
	 * 
	 *             public String synopsis;
	 * 
	 *             public String title;
	 * 
	 *             public boolean pageCountUncertain;
	 * @Join(field = "type_id", table = StoryTypeTable.class) public int typeId;
	 * 
	 *             } public static final Column[] ALL_COLUMNS = new Column[] {
	 *             new Column("id"), new Column("title"), new Column("feature"),
	 *             new Column("sequence_number"), new Column("page_count"), new
	 *             Column("issue_id"), new Column("script"), new
	 *             Column("pencils"), new Column("inks"), new Column("colors"),
	 *             new Column("letters"), new Column("editing"), new
	 *             Column("genre"), new Column("characters"), new
	 *             Column("synopsis"), new Column("reprint_notes"), new
	 *             Column("modified"), new Column("notes"), new
	 *             Column("type_id"), new Column("job_number"), new
	 *             Column("page_count_uncertain") };
	 * 
	 *             public static final Column[] ALL_COLUMNS = new Column[] { new
	 *             Column("id"), new Column("number"), new Column("volume"), new
	 *             Column("series_id"), new Column("indicia_publisher_id"), new
	 *             Column("brand_id"), new Column("publication_date"), new
	 *             Column("key_date"), new Column("price"), new
	 *             Column("page_count"), new Column("indicia_frequency"), new
	 *             Column("editing"), new Column("notes"), new
	 *             Column("modified"), new Column("valid_isbn"), new
	 *             Column("variant_name"), new Column("barcode"), new
	 *             Column("title"), new Column("on_sale_date"), new
	 *             Column("rating") };
	 * 
	 *             ISSUE <-> Story [partOf]
	 * 
	 *             editors -> roles [intratable] editors -> genres everywhere
	 *             creator mapping we need to add editors editors <-> creators
	 *             [collaborate]
	 * 
	 *             characters -> CreativeWork.characters 2nd hop creators
	 *             associated with series, publisher [worked For] characters
	 *             associated with series, publisher[indicia, master]
	 */

}
