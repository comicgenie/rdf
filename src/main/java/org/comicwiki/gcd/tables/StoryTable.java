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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.Join;
import org.comicwiki.OrgLookupService;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.CreatorField;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.gcd.tables.joinrules.StoryAndIssueRule;
import org.comicwiki.gcd.tables.joinrules.StoryAndSeriesRule;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.CreatorAlias;
import org.comicwiki.model.CreatorRole;
import org.comicwiki.model.Genre;
import org.comicwiki.model.notes.ReprintNote;
import org.comicwiki.model.notes.StoryNote;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.comicwiki.model.schema.bib.ComicSeries;
import org.comicwiki.model.schema.bib.ComicStory;
import org.comicwiki.relations.ComicCharactersAssigner;
import org.comicwiki.relations.ComicCreatorAssigner;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Join(value = IssueTable.class, withRule = StoryAndIssueRule.class)
@Join(value = SeriesTable.class, withRule = StoryAndSeriesRule.class)
@Join(value = PublisherTable.class, leftKey = "fkPublisherId", leftField = "publisher")
@Join(value = StoryTypeTable.class, leftKey = "fkTypeId", leftField = "storyType", rightField = "name")
@Join(value = IndiciaPublisherTable.class, leftKey = "fkIndiciaPublisherId", leftField = "indiciaPublisher")
@Singleton
public class StoryTable extends BaseTable<StoryTable.StoryRow> {

	public static final class Columns {

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

	public static final class StoryRow extends TableRow<ComicStory> {

		public Collection<Person> alaises;

		public Collection<ComicCharacter> characters;

		public Collection<Person> colors;

		public Collection<CreatorAlias> creatorAliases = new HashSet<>(3);

		public Collection<Person> editing;

		public String feature;

		public String field_characters;

		public String field_colors;

		public String field_editing;

		public String field_letters;

		public String field_pencils;

		public String field_script;

		public String field_inks;

		/**
		 * gcd_indicia_publisher.id
		 */
		public int fkIndiciaPublisherId;

		/**
		 * gcd_series.id
		 */
		public int fkIssueId;

		/**
		 * gcd_publisher.id
		 */
		public int fkPublisherId;

		/**
		 * gcd_series.id
		 */
		public int fkSeriesId;

		/**
		 * gcd_story_type.id
		 */
		public int fkTypeId;

		public Collection<Genre> genre = new HashSet<>(3);

		public Organization indiciaPublisher;

		public Collection<Person> inks;

		public ComicStory instance = create(thingFactory);

		public ComicIssue issue;

		public String jobNumber;

		public Collection<Person> letters;

		public Date modified;;

		public Collection<String> notes;

		public Collection<ComicOrganization> organizations;

		public int pageCount;

		public boolean pageCountUncertain;

		public Collection<Person> pencils;

		public Organization publisher;

		public Collection<String> reprintNotes;

		public Collection<Person> script;

		public int sequenceNumber;

		public ComicSeries series;

		public String storyType;

		public String synopsis;

		public String title;

		public void addAliases(Collection<Person> a) {
			if (a == null) {
				return;
			}
			if (alaises == null) {
				alaises = new HashSet<>(3);
			}
			alaises.addAll(a);
		}

	}

	private static final String sInputTable = "gcd_story";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	private OrgLookupService comicOrganizations;

	private HashMap<String, Genre> genreCache = new HashMap<>();

	private final FieldParserFactory parserFactory;

	@Inject
	public StoryTable(SQLContext sqlContext, ThingFactory thingFactory,
			FieldParserFactory parserFactory,
			OrgLookupService comicOrganizations) {
		super(sqlContext, sParquetName);
		StoryTable.thingFactory = thingFactory;
		this.comicOrganizations = comicOrganizations;
		this.parserFactory = parserFactory;
	}

	@Override
	protected void parseFields(StoryRow storyRow) {
		if (!Strings.isNullOrEmpty(storyRow.field_characters)) {
			Fields.Character characterField = parseField(
					storyRow.field_characters, parserFactory.character(
							comicOrganizations, storyRow.instance));

			if (characterField != null) {
				storyRow.characters = characterField.comicCharacters;
				storyRow.organizations = characterField.comicOrganizations;
				characterField = null;
			}
		}

		if (!Strings.isNullOrEmpty(storyRow.field_colors)) {

			CreatorField colorCreatorField = parseField(storyRow.field_colors,
					parserFactory.creator(storyRow.instance));
			if (colorCreatorField != null) {
				storyRow.colors = colorCreatorField.creators;
				storyRow.addAliases(colorCreatorField.aliases);
				storyRow.creatorAliases
						.addAll(colorCreatorField.creatorAliases);
				colorCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.colorist);
			}
		}

		if (!Strings.isNullOrEmpty(storyRow.field_editing)) {
			CreatorField editingCreatorField = parseField(
					storyRow.field_editing,
					parserFactory.creator(storyRow.instance));
			if (editingCreatorField != null) {
				storyRow.editing = editingCreatorField.creators;
				storyRow.addAliases(editingCreatorField.aliases);
				storyRow.creatorAliases
						.addAll(editingCreatorField.creatorAliases);
				editingCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.editor);
			}
		}

		if (!Strings.isNullOrEmpty(storyRow.field_inks)) {
			CreatorField inksCreatorField = parseField(storyRow.field_inks,
					parserFactory.creator(storyRow.instance));
			if (inksCreatorField != null) {
				storyRow.inks = inksCreatorField.creators;
				storyRow.addAliases(inksCreatorField.aliases);
				storyRow.creatorAliases.addAll(inksCreatorField.creatorAliases);
				inksCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.inker);
			}
		}

		if (!Strings.isNullOrEmpty(storyRow.field_letters)) {
			CreatorField lettersCreatorField = parseField(
					storyRow.field_letters,
					parserFactory.creator(storyRow.instance));
			if (lettersCreatorField != null) {
				storyRow.letters = lettersCreatorField.creators;
				storyRow.addAliases(lettersCreatorField.aliases);
				storyRow.creatorAliases
						.addAll(lettersCreatorField.creatorAliases);
				lettersCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.letterist);
			}
		}

		if (!Strings.isNullOrEmpty(storyRow.field_pencils)) {
			CreatorField pencilsCreatorField = parseField(
					storyRow.field_pencils,
					parserFactory.creator(storyRow.instance));
			if (pencilsCreatorField != null) {
				storyRow.pencils = pencilsCreatorField.creators;
				storyRow.addAliases(pencilsCreatorField.aliases);
				storyRow.creatorAliases
						.addAll(pencilsCreatorField.creatorAliases);
				pencilsCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.penciller);
			}
		}
		if (!Strings.isNullOrEmpty(storyRow.field_script)) {
			CreatorField scriptCreatorField = parseField(storyRow.field_script,
					parserFactory.creator(storyRow.instance));
			if (scriptCreatorField != null) {
				storyRow.script = scriptCreatorField.creators;
				storyRow.addAliases(scriptCreatorField.aliases);
				storyRow.creatorAliases
						.addAll(scriptCreatorField.creatorAliases);
				scriptCreatorField.creatorAliases
						.forEach(c -> c.role = CreatorRole.writer);
			}
		}
	}

	@Override
	public StoryRow process(Row row) throws IOException {
		StoryRow storyRow = new StoryRow();

		storyRow.field_colors = row.getString(Columns.COLORS);
		storyRow.field_characters = row.getString(Columns.CHARACTERS);
		storyRow.field_editing = row.getString(Columns.EDITING);
		storyRow.field_letters = row.getString(Columns.LETTERS);
		storyRow.field_pencils = row.getString(Columns.PENCILS);
		storyRow.field_script = row.getString(Columns.SCRIPT);
		storyRow.field_inks = row.getString(Columns.INKS);

		storyRow.title = row.getString(Columns.TITLE);
		storyRow.instance.name = storyRow.title;
		storyRow.feature = row.getString(Columns.FEATURE);

		storyRow.jobNumber = row.getString(Columns.JOB_NUMBER);
		storyRow.modified = row.getTimestamp(Columns.MODIFIED);
		storyRow.pageCountUncertain = row
				.isNullAt(Columns.PAGE_COUNT_UNCERTAIN) ? false : row
				.getBoolean(Columns.PAGE_COUNT_UNCERTAIN);
		if (!row.isNullAt(Columns.GENRE)) {
			Collection<String> genres = parseField(Columns.GENRE, row,
					parserFactory.string(true));

			for (String name : genres) {
				if (!genreCache.containsKey(name)) {
					Genre genre = thingFactory.create(Genre.class);
					genre.name = name;
					genreCache.put(name, genre);
				}
				storyRow.genre.add(genreCache.get(name));
			}
		}

		if (!row.isNullAt(Columns.NOTES)) {
			storyRow.notes = parseField(Columns.NOTES, row,
					parserFactory.string(false));
		}
		if (!row.isNullAt(Columns.REPRINT_NOTES)) {
			storyRow.reprintNotes = parseField(Columns.REPRINT_NOTES, row,
					parserFactory.string(false));
		}
		storyRow.synopsis = row.getString(Columns.SYNPOSIS);

		if (!row.isNullAt(Columns.TYPE_ID)) {
			storyRow.fkTypeId = row.getInt(Columns.TYPE_ID);
		}
		if (!row.isNullAt(Columns.PAGE_COUNT)) {
			storyRow.pageCount = ((BigDecimal) row.get(Columns.PAGE_COUNT))
					.intValue();
		}
		if (!row.isNullAt(Columns.SEQUENCE_NUMBER)) {
			storyRow.sequenceNumber = row.getInt(Columns.SEQUENCE_NUMBER);
		}

		if (!row.isNullAt(Columns.ISSUE_ID)) {
			storyRow.fkIssueId = row.getInt(Columns.ISSUE_ID);
		}

		if (!row.isNullAt(Columns.ID)) {
			storyRow.id = row.getInt(Columns.ID);
			add(storyRow);
		}
		return storyRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	public void transform(StoryRow row) {
		super.transform(row);
		ComicStory story = row.instance;
		// TODO: need series internal id to GCD
		// story.urls.add(URI.create("http://www.comics.org/series/" +
		// row.series. "#" + row.id));
		if (row.genre != null) {
			for (Genre g : row.genre) {
				story.addGenre(g.instanceId);
				if (row.series != null) {
					row.series.addGenre(story.genres);
				}
			}
		}

		if (row.characters != null) {
			ComicCharactersAssigner charAssign = new ComicCharactersAssigner(
					row.characters);
			if (row.genre != null) {
				charAssign.genres(row.genre);
			}
			charAssign.story(story);
		}

		ComicCreatorAssigner creatorAssigner = new ComicCreatorAssigner(
				row.colors, row.inks, row.letters, row.pencils, row.script,
				row.editing);
		creatorAssigner.colleagues();
		creatorAssigner.jobTitles();
		creatorAssigner.characters(row.characters);
		creatorAssigner.comicOrganizations(row.organizations);

		if (row.publisher != null) {
			story.addPublisher(row.publisher.instanceId);
			creatorAssigner.organization(row.publisher);

		}
		if (row.indiciaPublisher != null) {
			story.addPublisherImprints(row.indiciaPublisher.instanceId);
			creatorAssigner.organization(row.indiciaPublisher);
		}

		if (row.reprintNotes != null && !row.reprintNotes.isEmpty()) {
			ReprintNote rn = thingFactory.create(ReprintNote.class);
			rn.addReprintNote(row.reprintNotes);
			story.addReprintNote(rn.instanceId);
		}

		if (row.notes != null && !row.notes.isEmpty()) {
			StoryNote storyNote = thingFactory.create(StoryNote.class);
			storyNote.note.addAll(row.notes);
			story.addStoryNote(storyNote.instanceId);
		}

		if (row.series != null) {
			story.addIsPartOf(row.series.instanceId);
			row.series.addHasPart(story.instanceId);
		}

		if (row.issue != null) {
			row.issue.addHasPart(row.instance.instanceId);
			row.issue.addGenre(row.instance.genres);
			row.instance.addIsPartOf(row.issue.instanceId);
		}

		story.headline = row.feature;

		story.storyType = row.storyType;
		if (!Strings.isNullOrEmpty(row.synopsis)) {
			story.addDescription(row.synopsis);
		}
		story.jobCode = row.jobNumber;
		story.position = row.sequenceNumber;
		story.pageCountUncertain = row.pageCountUncertain;
		story.pageCount = row.pageCount;
		// story.pageStart - requires more than one row...
		/**
		 * We can take sequences + page counts to determine pageStart and
		 * pageEnd
		 */
		if (row.organizations != null) {
			row.organizations.forEach(e -> story
					.addFictionalOrganization(e.instanceId));
		}
	}
}
