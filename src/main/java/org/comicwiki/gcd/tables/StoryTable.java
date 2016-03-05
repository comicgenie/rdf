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
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.gcd.fields.FieldParserFactory;
import org.comicwiki.gcd.tables.joinrules.StoryAndIssueRule;
import org.comicwiki.gcd.tables.joinrules.StoryAndSeriesRule;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.Genre;
import org.comicwiki.model.ReprintNote;
import org.comicwiki.model.StoryNote;
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

@Join(value = IssueTable.class, withRule = StoryAndIssueRule.class)
@Join(value = SeriesTable.class, withRule = StoryAndSeriesRule.class)
@Join(value = PublisherTable.class, leftKey = "fkPublisherId", leftField = "publisher")
@Join(value = StoryTypeTable.class, leftKey = "fkTypeId", leftField = "storyType", rightField = "name")
@Join(value = IndiciaPublisherTable.class, leftKey = "fkIndiciaPublisherId", leftField = "indiciaPublisher")
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

		public Collection<ComicCharacter> characters;

		public Collection<Person> colors;

		public Collection<Person> editing;

		public String feature;

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
		
		public ComicIssue issue;

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
	public StoryRow process(Row row) throws IOException {
		StoryRow storyRow = new StoryRow();

		Fields.Character characterField = parseField(Columns.CHARACTERS, row,
				parserFactory.character(comicOrganizations, storyRow.instance));

		if (characterField != null) {
			storyRow.characters = characterField.comicCharacters;
			storyRow.organizations = characterField.comicOrganizations;
		}

		storyRow.title = row.getString(Columns.TITLE);
		storyRow.feature = row.getString(Columns.FEATURE);

		storyRow.colors = parseField(Columns.COLORS, row,
				parserFactory.creator());
		storyRow.editing = parseField(Columns.EDITING, row,
				parserFactory.creator());
		storyRow.inks = parseField(Columns.INKS, row, parserFactory.creator());
		storyRow.letters = parseField(Columns.LETTERS, row,
				parserFactory.creator());
		storyRow.pencils = parseField(Columns.PENCILS, row,
				parserFactory.creator());
		storyRow.script = parseField(Columns.SCRIPT, row,
				parserFactory.creator());

		storyRow.jobNumber = row.getString(Columns.JOB_NUMBER);
		storyRow.modified = row.getTimestamp(Columns.MODIFIED);
		storyRow.pageCountUncertain = row
				.isNullAt(Columns.PAGE_COUNT_UNCERTAIN) ? false : row
				.getBoolean(Columns.PAGE_COUNT_UNCERTAIN);
		if (!row.isNullAt(Columns.GENRE)) {
			Collection<String> genres = parseField(
					Columns.GENRE,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings()
								.split(r.getString(f).toUpperCase()));
					});

			for (String name : genres) {
				if (!cache.containsKey(name)) {
					Genre genre = thingFactory.create(Genre.class);
					genre.name = name;
					genreCache.put(name, genre);
				}
				storyRow.genre.add(genreCache.get(name));
			}
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
			storyRow.reprintNotes = parseField(
					Columns.REPRINT_NOTES,
					row,
					(f, r) -> {
						return Sets.newHashSet(Splitter.on(';').trimResults()
								.omitEmptyStrings().split(r.getString(f)));
					});
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
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl,
				10000);
	}

	@Override
	public void transform(StoryRow row) {
		super.transform(row);
		ComicStory story = row.instance;
		for (Genre g : row.genre) {
			story.genres.add(g.instanceId);
			row.series.genres.addAll(story.genres);
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
			story.publishers.add(row.publisher.instanceId);
			creatorAssigner.organization(row.publisher);

		}
		if (row.indiciaPublisher != null) {
			story.publisherImprints.add(row.indiciaPublisher.instanceId);
			creatorAssigner.organization(row.indiciaPublisher);
		}

		if (row.reprintNotes != null && !row.reprintNotes.isEmpty()) {
			ReprintNote rn = thingFactory.create(ReprintNote.class);
			rn.note.addAll(row.reprintNotes);
			story.reprintNote.add(rn.instanceId);
		}

		if (row.notes != null && !row.notes.isEmpty()) {
			StoryNote storyNote = thingFactory.create(StoryNote.class);
			storyNote.note.addAll(row.notes);
			story.storyNote.add(storyNote.instanceId);
		}

		if (row.series != null) {
			story.isPartOf.add(row.series.instanceId);
			row.series.hasParts.add(story.instanceId);
		}
		
		if(row.issue != null) {
			row.issue.hasParts.add(row.instance.instanceId);
			row.issue.genres.addAll(row.instance.genres);
			row.instance.isPartOf.add(row.issue.instanceId);		
		}
		
		story.headline = row.feature;
		story.name = row.title;
		story.storyType = row.storyType;
		if (!Strings.isNullOrEmpty(row.synopsis)) {
			story.description.add(row.synopsis);
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
			row.organizations.forEach(e -> story.fictionalOrganizations
					.add(e.instanceId));
		}
	}
}
