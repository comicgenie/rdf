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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Stream;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.ComicKeyRepository;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.gcd.CharacterFieldParser;
import org.comicwiki.gcd.CreatorFieldParser;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.schema.ComicStory;
import org.comicwiki.model.schema.Person;
import org.comicwiki.repositories.ComicCharacterRepository;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class StoryTable extends BaseTable<StoryTable.StoryRow> {
	ComicCharacterRepository cRepo = new ComicCharacterRepository();
	int i = 0;
	
	public void preExport() {
		PersonNameMatcher imp = new PersonNameMatcher();
		try {
			imp.load(new File("./src/main/resources/names/yob2014.txt"));
			imp.loadLastNames(new File("./src/main/resources/names/lastname.txt"));
			cRepo.addGender(imp);

			try {
				cRepo.print();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void exportRowToRepositories(StoryRow row) {
		super.exportRowToRepositories(row);
	
		for(ComicCharacter cc : row.characters) {
			System.out.println(i +":" + "NAME: " + cc.name);
			cRepo.add(cc);
			if(i++ > 100) {
				
				break;
			}
		}

	
		//export to StoryRepository, CharacterRepository, CreatorRepository
	}

	private static final class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("title"), new Column("feature"),
				new Column("sequence_number"), new Column("page_count"),
				new Column("issue_id"), new Column("script"),
				new Column("pencils"), new Column("inks"),
				new Column("colors"), new Column("letters"),
				new Column("editing"), new Column("genre"),
				new Column("characters"), new Column("synopsis"),
				new Column("reprint_notes"), new Column("modified"),
				new Column("notes"), new Column("type_id"),
				new Column("job_number"), new Column("page_count_uncertain") };
		public static final int CHARACTERS = 13;
		public static final int COLORS = 9;
		public static final int EDITING = 11;
		public static final int FEATURE = 2;
		public static final int GENRE = 12;
		public static final int ID = 0;
		public static final int INKS = 8;
		public static final int ISSUE_ID = 5;
		public static final int JOB_NUMBER = 19;
		public static final int LETTERS = 10;
		public static final int MODIFIED = 16;
		public static final int NOTES = 17;
		public static final int PAGE_COUNT = 4;
		public static final int PAGE_COUNT_UNCERTAIN = 20;
		public static final int PENCILS = 7;
		public static final int REPRINT_NOTES = 15;
		public static final int SCRIPT = 6;
		public static final int SEQUENCE_NUMBER = 3;
		public static final int SYNPOSIS = 14;
		public static final int TITLE = 1;

		public static final int TYPE_ID = 18;
	}

	public static class Fields {
		public static class Character {
			public Collection<ComicCharacter> comicCharacters = new HashSet<>(5);

			public Collection<ComicOrganization> comicOrganizations = new HashSet<>(
					2);
		}
	}

	public static final class StoryRow extends TableRow {

		public Collection<ComicCharacter> characters;

		public Collection<Person> colors;

		public ComicStory comicStory;

		public Collection<Person> editing;

		public String feature;

		public Collection<String> genre;

		public Collection<Person> inks;

		public int issueId;

		public String jobNumber;

		public Collection<Person> letters;

		public Date modified;

		public Collection<String> notes;

		public Collection<ComicOrganization> organizations;

		public BigDecimal pageCount;

		public boolean pageCountUncertain;

		public Collection<Person> pencils;;

		public String publisher;

		public Collection<String> reprintNotes;;

		public Collection<Person> script;

		public int sequenceNumber;

		public String storyType;

		public String synopsis;

		public String title;

		public int typeId;

	}

	private static final String sInputTable = "gcd_story";

	private static final String sParquetName = sInputTable + ".parquet";

	public StoryTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	public void addEditors(StoryTypeTable storyTypeTable) {
		cache.values().forEach(story -> {
			story.storyType = storyTypeTable.cache.get(story.typeId).name;
		});
	}

	public void addPublishers(StoryTypeTable storyTypeTable) {
		cache.values().forEach(story -> {
			story.storyType = storyTypeTable.cache.get(story.typeId).name;
		});
	}

	public void addStoryTypes(StoryTypeTable storyTypeTable) {
		cache.values().forEach(story -> {
			story.storyType = storyTypeTable.cache.get(story.typeId).name;
		});
	}

	@Override
	public StoryRow process(Row row) throws IOException {
		StoryRow storyRow = new StoryRow();

		Fields.Character characterField = parseField(Columns.CHARACTERS, row,
				new CharacterFieldParser(
						new File("./src/main/resources/comics")));// TODO: pull
																	// from jar

		if (characterField != null) {
			storyRow.characters = characterField.comicCharacters;
			storyRow.organizations = characterField.comicOrganizations;
		}

		storyRow.title = row.getString(Columns.TITLE);
		storyRow.feature = row.getString(Columns.FEATURE);

		storyRow.colors = parseField(Columns.COLORS, row,
				new CreatorFieldParser());
		storyRow.editing = parseField(Columns.EDITING, row,
				new CreatorFieldParser());
		storyRow.inks = parseField(Columns.INKS, row, new CreatorFieldParser());
		storyRow.letters = parseField(Columns.LETTERS, row,
				new CreatorFieldParser());
		storyRow.pencils = parseField(Columns.PENCILS, row,
				new CreatorFieldParser());
		storyRow.script = parseField(Columns.SCRIPT, row,
				new CreatorFieldParser());

		storyRow.jobNumber = row.getString(Columns.JOB_NUMBER);
		storyRow.modified = row.getTimestamp(Columns.MODIFIED);
		storyRow.pageCountUncertain = row
				.isNullAt(Columns.PAGE_COUNT_UNCERTAIN) ? false : row
				.getBoolean(Columns.PAGE_COUNT_UNCERTAIN);

		// storyRow.notes =
		if (!row.isNullAt(Columns.GENRE)) {
			storyRow.genre = parseField(
					Columns.GENRE,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings().split(r.getString(f)));
					});
		}
		if (!row.isNullAt(Columns.NOTES)) {
			storyRow.notes = parseField(
					Columns.NOTES,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings().split(r.getString(f)));
					});
		}
		if (!row.isNullAt(Columns.REPRINT_NOTES)) {
			storyRow.notes = parseField(
					Columns.REPRINT_NOTES,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings().split(r.getString(f)));
					});
		}
		storyRow.synopsis = row.getString(Columns.SYNPOSIS);

		if (!row.isNullAt(Columns.TYPE_ID)) {
			storyRow.typeId = row.getInt(Columns.TYPE_ID);
		}
		if (!row.isNullAt(Columns.PAGE_COUNT)) {
			storyRow.pageCount = (BigDecimal) row.get(Columns.PAGE_COUNT);
		}
		if (!row.isNullAt(Columns.SEQUENCE_NUMBER)) {
			storyRow.sequenceNumber = row.getInt(Columns.SEQUENCE_NUMBER);
		}

		if (!row.isNullAt(Columns.ISSUE_ID)) {
			storyRow.issueId = row.getInt(Columns.ISSUE_ID);
		}

		if (!row.isNullAt(Columns.ID)) {
			storyRow.id = row.getInt(Columns.ID);
			add(storyRow);
		}
		return storyRow;
	}

	@Override
	public void assignRelations(StoryRow row) {
		super.assignRelations(row);
		Stream<Person> creators = Stream.of(row.inks, row.colors, row.editing)
				.flatMap(Collection::stream);
	
		
		// RelationsAssigner.colleagues(creators);
		// RelationsAssigner.creatorRoles(row.colors, CreatorRole.colorist);
		// RelationsAssigner.creatorRoles(row.inks, CreatorRole.inker);
		// RelationsAssigner.creatorRoles(row.letters, CreatorRole.letterist);
		// RelationsAssigner.creatorRoles(row.pencils, CreatorRole.penclier);
		// RelationsAssigner.creatorRoles(row.script, CreatorRole.writer);

		// genderOfPersons
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl,
				10000);
	}
}
