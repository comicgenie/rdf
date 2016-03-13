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

import static org.comicwiki.gcd.fields.CharacterFieldCleaner.cleanAll;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.spark.sql.Row;
import org.comicwiki.FieldParser;
import org.comicwiki.IRI;
import org.comicwiki.OrgLookupService;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CharacterFieldLexer;
import org.comicwiki.gcd.parser.CharacterFieldParser.CharactersContext;
import org.comicwiki.gcd.parser.CharacterFieldParser.OrganizationContext;
import org.comicwiki.gcd.parser.CharacterFieldParser.OrganizationOrCharacterContext;
import org.comicwiki.gcd.parser.CharacterFieldParser.RootContext;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.notes.ComicCharacterNote;
import org.comicwiki.model.notes.ComicOrganizationNote;
import org.comicwiki.model.notes.StoryNote;
import org.comicwiki.model.schema.bib.ComicStory;
import org.comicwiki.relations.ComicCharactersAssigner;
import org.comicwiki.relations.ComicOrganizationAssigner;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public final class CharacterFieldParser implements
		FieldParser<StoryTable.Fields.Character> {

	private static RootContext getContextOf(String textField,
			boolean failOnError) {
		CharacterFieldLexer lexer = new CharacterFieldLexer(
				new ANTLRInputStream(textField));
		 lexer.removeErrorListeners();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		org.comicwiki.gcd.parser.CharacterFieldParser parser = new org.comicwiki.gcd.parser.CharacterFieldParser(
				tokens);
		 parser.removeErrorListeners();
		if (failOnError) {
			//parser.setErrorHandler(new BailErrorStrategy());
		}
		return parser.root();
	}

	private StoryTable.Fields.Character characterFields;

	private final HashMap<String, ComicCharacter> charactersCache = new HashMap<>();

	private final OrgLookupService comicOrganizationsLookup;

	private ComicStory comicStory;

	private boolean failOnError = true;

	private final HashMap<String, ComicOrganization> organizationsCache = new HashMap<>();

	private final ThingFactory thingFactory;

	protected CharacterFieldParser(ThingFactory thingFactory,
			OrgLookupService comicOrganizations) {
		this.thingFactory = thingFactory;
		this.comicOrganizationsLookup = comicOrganizations;
	}

	public void clear() {
		comicStory = null;
		characterFields = null;
		organizationsCache.clear();
		charactersCache.clear();
	}

	protected void setStory(ComicStory comicStory) {
		this.comicStory = comicStory;
		characterFields = new StoryTable.Fields.Character();
	}

	private StoryNote addStoryNote(String note, ComicCharacter character,
			ComicStory story) {
		ComicCharacterNote storyNote = thingFactory
				.create(ComicCharacterNote.class);
		storyNote.story = story.instanceId;
		storyNote.comicCharacter = character.instanceId;
		storyNote.note.add(note);
		story.addStoryNote(storyNote.instanceId);
		return storyNote;
	}

	private StoryNote addStoryNote(String note, ComicOrganization org,
			ComicStory story) {
		ComicOrganizationNote storyNote = thingFactory
				.create(ComicOrganizationNote.class);
		storyNote.story = story.instanceId;
		storyNote.comicOrganization = org.instanceId;
		storyNote.note.add(note);
		story.addStoryNote(storyNote.instanceId);
		return storyNote;
	}

	private void assignColleagues() {
		for (ComicOrganization comicOrganization : organizationsCache.values()) {
			Collection<ComicCharacter> team = new HashSet<>();
			for (IRI member : comicOrganization.members) {
				ComicCharacter cc = getCharacter(member);
				if (cc == null) {

				} else {
					team.add(cc);
				}
			}
			if (!team.isEmpty()) {
				ComicCharactersAssigner charAssigner = new ComicCharactersAssigner(
						team);
				charAssigner.colleagues();
			}
		}
	}

	private ComicCharacter createComicCharacter(String name) {
		ComicCharacter comicCharacter = charactersCache.get(name);
		if (comicCharacter != null) {
			return comicCharacter;
		}
		comicCharacter = thingFactory.create(ComicCharacter.class);
		comicCharacter.name = name;
		charactersCache.put(name, comicCharacter);
		return comicCharacter;
	}

	private ComicOrganization createOrganization(String name) {
		ComicOrganization comicOrganization = organizationsCache.get(name);
		if (comicOrganization != null) {
			return comicOrganization;
		}
		comicOrganization = thingFactory.create(ComicOrganization.class);
		comicOrganization.name = name;
		organizationsCache.put(name, comicOrganization);
		return comicOrganization;
	}

	protected ComicCharacter fillCharacter(ComicCharacter character,
			Collection<String> aliases, Collection<String> notes) {
		if (comicStory != null) {
			for (String note : notes) {
				addStoryNote(note, character, comicStory);
			}
		}

		for (String name : aliases) {
			ComicCharacter aliasCharacter = createComicCharacter(name);
			character.addIdentity(aliasCharacter.instanceId);
			aliasCharacter.addIdentity(character.instanceId);
		}
		return character;
	}

	protected ComicOrganization fillOrganization(
			ComicOrganization organization, Collection<String> members,
			Collection<String> notes) {
		if (comicStory != null) {
			for (String note : notes) {
				addStoryNote(note, organization, comicStory);
			}
		}

		for (String name : members) {
			ComicCharacter member = createComicCharacter(name);
			organization.members.add(member.instanceId);
			member.addIdentity(organization.instanceId);
			member.addMemberOf(organization.instanceId);
		}
		return organization;
	}

	private ComicCharacter getCharacter(IRI iri) {
		for (ComicCharacter comicCharacter : charactersCache.values()) {
			if (comicCharacter.instanceId.equals(iri)) {
				return comicCharacter;
			}
		}
		return null;
	}

	private boolean isOrganization(String entityName) {
		return comicOrganizationsLookup.isOrganization(entityName);
	}

	@Override
	public StoryTable.Fields.Character parse(int field, Row row) {
		if (row.isNullAt(field)) {
			return null;
		}
		return parse(row.getString(field));
	}

	public StoryTable.Fields.Character parse(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return characterFields;
		}

		RootContext rootContext = getContextOf(cleanAll(text), failOnError);

		for (CharactersContext charactersContext : rootContext.characters()) {
			charactersContext.organizationOrCharacter().forEach(
					e -> processOrganizationOrCharacter(e));
		}

		characterFields.comicCharacters = new HashSet<>(
				charactersCache.values());
		characterFields.comicOrganizations = new HashSet<>(
				organizationsCache.values());

		assignColleagues();
		organizationsCache.clear();
		charactersCache.clear();
		return characterFields;
	}

	private void processOrganization(OrganizationContext e) {
		String orgName = e.WORD().getText();
		ComicOrganization organization = createOrganization(orgName);
		state.organizationState(organization);
		e.characters().organizationOrCharacter()
				.forEach(e1 -> processOrganizationOrCharacter(e1));

	}

	private void processOrganizationOrCharacter(
			OrganizationOrCharacterContext oocContext) {
		if (oocContext.organization() != null
				&& !oocContext.organization().isEmpty()) {
			processOrganization(oocContext.organization());
			return;
		}

		Collection<String> localNotes = Lists.newArrayList();

		if (oocContext.noteTypes() != null) {
			// System.out.println("NoteType:" +
			// oocContext.noteTypes().getText());
			state.noteTypeState(oocContext.noteTypes().getText());
		}
		if (state.currentState == State.NOTE_TYPE_STATE) {
			localNotes.add(state.note);
		}

		if (oocContext.note() != null) {
			// System.out.println("Note:" + oocContext.note().getText());
			for (TerminalNode note : oocContext.note().WORD()) {
				localNotes.add(note.getText());
			}
		}

		// System.out.println("OrganizationOrCharacter:");
		// System.out.println("---- " + oocContext.WORD());
		if (oocContext.WORD() == null) {
			return;
		}

		String entityName = oocContext.WORD().getText();
		if (Strings.isNullOrEmpty(entityName)) {
			return;
		}

		if (isOrganization(entityName)) {
			// TODO: normalize name - orgs IRI already created
			ComicOrganization comicOrganization = createOrganization(entityName);
			Collection<String> members = Lists.newArrayList();
			if (oocContext.membersOrAliases() != null) {
				for (OrganizationOrCharacterContext orgCtx : oocContext
						.membersOrAliases().organizationOrCharacter()) {
					if (orgCtx.WORD() != null) {
						members.add(orgCtx.WORD().getText());
					}

					processOrganizationOrCharacter(orgCtx);
				}
			}
			fillOrganization(comicOrganization, members, localNotes);
		} else {
			ComicCharacter comicCharacter = createComicCharacter(entityName);
			if (state.organization != null) {
				ComicOrganizationAssigner assigner = new ComicOrganizationAssigner(
						state.organization);
				assigner.characters(Lists.newArrayList(comicCharacter));
			}
			Collection<String> aliases = Lists.newArrayList();
			if (oocContext.membersOrAliases() != null) {
				for (OrganizationOrCharacterContext cc : oocContext
						.membersOrAliases().organizationOrCharacter()) {
					if (cc.WORD() != null) {
						aliases.add(cc.WORD().getText());
					}
					processOrganizationOrCharacter(cc);
				}
			}

			fillCharacter(comicCharacter, aliases, localNotes);
		}
	}

	private State state = new State();

	private class State {

		public static final int ORGANIZATION_STATE = 2;

		public static final int NOTE_TYPE_STATE = 1;

		public static final int NONE = 0;

		public int currentState;

		public ComicOrganization organization;

		public String note;

		public void noState() {
			currentState = NONE;
			note = null;
			organization = null;
		}

		public void noteTypeState(String note) {
			this.note = note;
			currentState = NOTE_TYPE_STATE;
			organization = null;
		}

		public void organizationState(ComicOrganization organization) {
			this.organization = organization;
			currentState = ORGANIZATION_STATE;
			note = null;
		}
	}
}
