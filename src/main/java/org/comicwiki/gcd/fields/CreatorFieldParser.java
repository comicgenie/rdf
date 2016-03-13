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
package org.comicwiki.gcd.fields;

import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.spark.sql.Row;
import org.comicwiki.Add;
import org.comicwiki.FieldParser;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CreatorLexer;
import org.comicwiki.gcd.parser.CreatorParser;
import org.comicwiki.gcd.parser.CreatorParser.CreatorContext;
import org.comicwiki.gcd.parser.CreatorParser.CreatorsContext;
import org.comicwiki.model.CreatorAlias;
import org.comicwiki.model.notes.CreatorIssueNote;
import org.comicwiki.model.notes.CreatorStoryNote;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.comicwiki.model.schema.bib.ComicStory;

import com.google.common.base.Strings;

public final class CreatorFieldParser extends BaseFieldParser implements
		FieldParser<CreatorField> {

	private static CreatorsContext getContextOf(String textField,
			boolean failOnError) {
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(textField));
		lexer.removeErrorListeners();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CreatorParser parser = new CreatorParser(tokens);
		parser.removeErrorListeners();
		if (failOnError) {
			// parser.setErrorHandler(new BailErrorStrategy());
		}
		return parser.creators();
	}

	private ComicStory comicStory;

	private final ThingFactory thingFactory;

	private ComicIssue comicIssue;

	protected CreatorFieldParser(ComicStory comicStory,
			ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
		this.comicStory = comicStory;
	}

	protected CreatorFieldParser(ComicIssue comicIssue,
			ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
		this.comicIssue = comicIssue;
	}

	private CreatorAlias addAlias(Person creator, Person alias, ComicIssue issue) {
		CreatorAlias creatorAlias = thingFactory.create(CreatorAlias.class);
		if (alias != null) {
			creatorAlias.alias = alias.instanceId;
		}

		if (creator != null) {
			creatorAlias.creator = creator.instanceId;
		}

		creatorAlias.issue = issue.instanceId;
		issue.addCreatorAlias(creatorAlias.instanceId);
		return creatorAlias;
	}

	private CreatorAlias addAlias(Person creator, Person alias, ComicStory story) {
		CreatorAlias creatorAlias = thingFactory.create(CreatorAlias.class);
		if (alias != null) {
			creatorAlias.alias = alias.instanceId;
		}

		if (creator != null) {
			creatorAlias.creator = creator.instanceId;
		}

		creatorAlias.story = story.instanceId;
		story.addCreatorAlias(creatorAlias.instanceId);
		return creatorAlias;
	}

	private CreatorIssueNote addCreatorNote(String note, Person creator,
			ComicIssue issue) {
		if (creator == null || Strings.isNullOrEmpty(note)) {
			return null;
		}
		CreatorIssueNote storyNote = thingFactory
				.create(CreatorIssueNote.class);
		storyNote.issue = issue.instanceId;
		storyNote.creator = creator.instanceId;
		storyNote.note.add(note);
		issue.addIssueNote(storyNote.instanceId);
		return storyNote;
	}

	private CreatorStoryNote addCreatorNote(String note, Person creator,
			ComicStory story) {
		if (creator == null || Strings.isNullOrEmpty(note)) {
			return null;
		}
		CreatorStoryNote storyNote = thingFactory
				.create(CreatorStoryNote.class);
		storyNote.story = story.instanceId;
		storyNote.creator = creator.instanceId;
		storyNote.note.add(note);
		story.addStoryNote(storyNote.instanceId);
		return storyNote;
	}

	@Override
	public CreatorField parse(int field, Row row) {
		String input = row.getString(field);
		if (Strings.isNullOrEmpty(input)) {
			return null;
		}
		return this.parse(input);
	}

	public CreatorField parse(String field) {
		ArrayList<Person> creators = new ArrayList<>(5);
		ArrayList<Person> aliases = new ArrayList<>(5);
		ArrayList<CreatorAlias> creatorAliases = new ArrayList<>(5);

		CreatorsContext creatorsContext = getContextOf(field, true);
		for (CreatorContext ctx : creatorsContext.creator()) {
			Person creatorPerson = null, aliasPerson = null;
			boolean isCreatorUncertain = false;
			String name = getValue(ctx.WORD());
			if (!Strings.isNullOrEmpty(name)) {
				if (!name.equals("?")) {
					creatorPerson = thingFactory.create(Person.class);
					creators.add(creatorPerson);
					if (name.contains("?")) {
						isCreatorUncertain = true;
						name = name.replaceAll("\\?", "").trim();
					}
					creatorPerson.name = name;
				} else {
					isCreatorUncertain = true;
				}
			}

			String alias = getValue(ctx.ALIAS());
			if (!Strings.isNullOrEmpty(alias)) {
				// remove as
				aliasPerson = thingFactory.create(Person.class);
				aliasPerson.name = alias;
				aliases.add(aliasPerson);
				CreatorAlias ca = comicStory != null ? addAlias(creatorPerson,
						aliasPerson, comicStory) : addAlias(creatorPerson,
						aliasPerson, comicIssue);
				if (ca != null) {
					creatorAliases.add(ca);
				}
			}

			String notes = getValue(ctx.NOTES());
			if (!Strings.isNullOrEmpty(notes)) {
				if (comicStory != null) {
					CreatorStoryNote storyNote = addCreatorNote(notes,
							creatorPerson, comicStory);
					if (storyNote != null) {
						storyNote.isCreatorUncertain = isCreatorUncertain;
						addCreatorNote(notes, aliasPerson, comicStory);
					}

				} else {
					CreatorIssueNote storyNote = addCreatorNote(notes,
							creatorPerson, comicIssue);
					if (storyNote != null) {
						storyNote.isCreatorUncertain = isCreatorUncertain;
						addCreatorNote(notes, aliasPerson, comicIssue);
					}
				}
			}

		}
		CreatorField creatorField = new CreatorField();
		creatorField.aliases = Add.toArray(aliases, Person.class);
		creatorField.creators = Add.toArray(creators, Person.class);
		creatorField.creatorAliases = Add.toArray(creatorAliases,
				CreatorAlias.class);
		return creatorField;
		// remove typeset
	}
}
