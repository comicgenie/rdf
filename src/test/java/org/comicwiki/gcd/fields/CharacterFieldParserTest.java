package org.comicwiki.gcd.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.comicwiki.IRI;
import org.comicwiki.IRICache;
import org.comicwiki.OrgLookupService;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CharacterFieldLexer;
import org.comicwiki.gcd.parser.CharacterFieldParser;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.notes.ComicCharacterNote;
import org.comicwiki.model.notes.StoryNote;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.model.schema.bib.ComicStory;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CharacterFieldParserTest {

	private static IRI assertHasName(Collection<ComicCharacter> characters,
			String name) throws Exception {
		boolean hasName = false;
		IRI iri = null;
		for (ComicCharacter cc : characters) {
			if (name.equals(cc.name)) {
				hasName = true;
				iri = cc.instanceId;
			}
		}
		if (!hasName) {
			throw new Exception("Name not found: " + name);
		}

		return iri;
	}

	private static IRI assertHasOrganization(
			Collection<ComicOrganization> organizations, String name)
			throws Exception {
		boolean hasName = false;
		IRI iri = null;
		for (ComicOrganization cc : organizations) {
			if (name.equals(cc.name)) {
				hasName = true;
				iri = cc.instanceId;
			}
		}
		if (!hasName) {
			throw new Exception("Name not found: " + name);
		}

		return iri;
	}

	private static org.comicwiki.gcd.fields.CharacterFieldParser createParser(
			String input, OrgLookupService service) {
		return createParser(input, service, createThingFactory(), null);
	}

	private static org.comicwiki.gcd.fields.CharacterFieldParser createParser(
			String input, OrgLookupService service, ThingFactory thingFactory,
			ComicStory comicStory) {
		org.comicwiki.gcd.fields.CharacterFieldParser cfp = new org.comicwiki.gcd.fields.CharacterFieldParser(
				thingFactory, service);
		cfp.setStory(comicStory);
		return cfp;

	}

	private static ThingFactory createThingFactory() {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());
		return new ThingFactory(thingCache);
	}

	@Test
	public void altOrganization() throws Exception {
		String input = "X-Men--Jean Grey; Rogue;";
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService());

		StoryTable.Fields.Character field = parser.parse(input);
		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(2, characters.size());
		assertEquals(1, field.comicOrganizations.size());

		ComicOrganization co = field.comicOrganizations.iterator().next();
		assertEquals(2, co.members.size());

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		assertTrue(co.members.contains(jean));
		assertTrue(co.members.contains(rogue));

		IRI xmen = assertHasOrganization(Lists.newArrayList(co), "X-Men");
		for (ComicCharacter cc : field.comicCharacters) {
			assertTrue(cc.memberOf.contains(xmen));
		}
	}

	@Test
	public void altOrganizationsTwo() throws Exception {
		String input = "X-Men--Jean Grey; Rogue;Y-Men--A;B";
		ThingFactory thingFactory = createThingFactory();
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, null);

		StoryTable.Fields.Character field = parser.parse(input);
		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());
		assertEquals(2, field.comicOrganizations.size());

		IRI xmen = assertHasOrganization(field.comicOrganizations, "X-Men");
		IRI ymen = assertHasOrganization(field.comicOrganizations, "Y-Men");

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI A = assertHasName(characters, "A");
		IRI B = assertHasName(characters, "B");

		ComicOrganization xmenOrg = (ComicOrganization) thingFactory.getCache()
				.get(xmen);
		ComicOrganization ymenOrg = (ComicOrganization) thingFactory.getCache()
				.get(ymen);
		ComicCharacter jeanChar = (ComicCharacter) thingFactory.getCache().get(
				jean);
		ComicCharacter rogueChar = (ComicCharacter) thingFactory.getCache()
				.get(rogue);
		ComicCharacter AChar = (ComicCharacter) thingFactory.getCache().get(A);
		ComicCharacter BChar = (ComicCharacter) thingFactory.getCache().get(B);

		assertEquals(2, xmenOrg.members.size());

		assertTrue(xmenOrg.members.contains(jean));
		assertTrue(xmenOrg.members.contains(rogue));

		assertTrue(ymenOrg.members.contains(A));
		assertTrue(ymenOrg.members.contains(B));

		assertTrue(jeanChar.memberOf.contains(xmen));
		assertFalse(jeanChar.memberOf.contains(ymen));

		assertTrue(AChar.memberOf.contains(ymen));
		assertFalse(AChar.memberOf.contains(xmen));

		assertTrue(jeanChar.colleagues.contains(rogue));
		assertFalse(jeanChar.colleagues.contains(A));
	}

	@Test
	public void embedded() throws Exception {
		String input = "Robin [Dick Grayson [NAME] ]";
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService());

		StoryTable.Fields.Character field = parser.parse(input);
		Collection<ComicCharacter> characters = field.comicCharacters;
		assertEquals(3, characters.size());
		IRI robin = assertHasName(characters, "Robin");
		IRI dick = assertHasName(characters, "Dick Grayson");
		IRI name = assertHasName(characters, "NAME");

		for (ComicCharacter cc : field.comicCharacters) {
			if (cc.instanceId.equals(robin)) {
				assertEquals(1, cc.identities.size());
				cc.identities.contains(dick);
			} else if (cc.instanceId.equals(dick)) {
				assertEquals(2, cc.identities.size());
				cc.identities.contains(robin);
				cc.identities.contains(name);
			} else if (cc.instanceId.equals(name)) {
				assertEquals(1, cc.identities.size());
				cc.identities.contains(dick);
			}
		}
	}

	@Test
	public void incompleteTokenCharacter() {
		String input = "Mr. Fantastic [X";
		OrgLookupService orgService = new OrgLookupService();

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, orgService);
		StoryTable.Fields.Character field = parser.parse(input);
		assertEquals(2, field.comicCharacters.size());
	}

	@Test
	public void noteInAlias() {
		String input = "Mr. Fantastic [WORD (note)]";

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService());
		StoryTable.Fields.Character field = parser.parse(input);
	}

	@Test
	public void incompleteTokenOrganization() {
		String input = "Fantastic Four [X";
		OrgLookupService orgService = new OrgLookupService(
				Lists.newArrayList("Fantastic Four"));

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, orgService);
		StoryTable.Fields.Character field = parser.parse(input);
		assertEquals(1, field.comicOrganizations.size());
		assertEquals(1, field.comicCharacters.size());
	}

	@Test
	public void noteTypeOnly() throws Exception {
		String input = "INTRODUCTION:";
		ThingFactory thingFactory = createThingFactory();

		OrgLookupService service = new OrgLookupService();

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, service, thingFactory, null);

		parser.parse(input);
	}

	//
	// John Winston; Pete Baxter; Tony Carter; Chief Taylor; Officer Ahearn;
	// Henry Louis; Mary Louis; The Carlson Gang [Carlson (villain); Walt
	// (villain, death); Michaels [as Mitchell] (villain, death); Gip Sands
	// (villain)]
	// @Test
	public void readSample() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		ComicStory story = thingFactory.create(ComicStory.class);
		Scanner s = new Scanner(new BufferedReader(new FileReader(
				"./src/test/resources/characters.txt")));
		int count = 0;
		while (s.hasNextLine()) {
			String input = s.nextLine();

			org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
					input, new OrgLookupService(), thingFactory, story);
			try {
				StoryTable.Fields.Character field = parser.parse(input);
				// System.out.println(field.comicCharacters);
				// System.out.println(field.comicOrganizations);
			} catch (Exception e) {
				System.out.println(count++ + " : " + input);

				// e.printStackTrace();
			}
		}
	}

	@Test
	public void simple() throws Exception {
		String input = "Robin [Dick Grayson]";

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService());

		StoryTable.Fields.Character field = parser.parse(input);

		assertEquals(2, field.comicCharacters.size());

		for (ComicCharacter cc : field.comicCharacters) {
			assertEquals(1, cc.identities.size());
			System.out.println(cc.instanceId);
			if (cc.instanceId.equals(new IRI("-1"))) {
				cc.identities.contains(new IRI("-2"));
			} else if (cc.instanceId.equals(new IRI("-2"))) {
				cc.identities.contains(new IRI("-1"));
			} else {
				throw new Exception();
			}
		}
	}

	@Test
	public void simpleLocalNoteWithColon() {
		String input = "Jim (origin:details)";
		OrgLookupService orgService = new OrgLookupService();
		ThingFactory thingFactory = createThingFactory();
		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, orgService, thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);
		assertEquals(1, field.comicCharacters.size());

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<StoryNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof StoryNote) {
				notes.add((StoryNote) thing);
			}
		}
		assertEquals(2, notes.size());
	}

	@Test
	public void simpleNoteInCache() throws Exception {
		String input = "Robin ( a note )";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		assertEquals(1, field.comicCharacters.size());

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<StoryNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof StoryNote) {
				notes.add((StoryNote) thing);
			}
		}

		assertEquals(1, notes.size());
		assertEquals("a note", notes.iterator().next().note.iterator().next());

	}

	@Test
	public void storyNoteAndTeam() throws Exception {
		String input = "GUESTS: Nick Fury; SHIELD;X-Men--Jean Grey; Rogue";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());
		assertEquals(1, field.comicOrganizations.size());

		IRI xmen = assertHasOrganization(field.comicOrganizations, "X-Men");

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI nick = assertHasName(characters, "Nick Fury");
		IRI shield = assertHasName(characters, "SHIELD");

		ComicOrganization xmenOrg = (ComicOrganization) thingFactory.getCache()
				.get(xmen);

		assertEquals(2, xmenOrg.members.size());

		assertTrue(xmenOrg.members.contains(jean));
		assertTrue(xmenOrg.members.contains(rogue));

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<ComicCharacterNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof ComicCharacterNote) {
				notes.add((ComicCharacterNote) thing);
			}
		}
		System.out.println(notes);
		assertEquals(2, notes.size());
		Iterator<ComicCharacterNote> it = notes.iterator();
		ComicCharacterNote storyNote1 = it.next();
		ComicCharacterNote storyNote2 = it.next();
		assertEquals("GUEST", storyNote1.note.iterator().next());
		assertEquals("GUEST", storyNote2.note.iterator().next());

		assertTrue(storyNote1.comicCharacter.equals(nick)
				|| storyNote1.comicCharacter.equals(shield));

	}

	@Test
	public void teamAndLocalNote() throws Exception {
		String input = "X-Men--Jean Grey; Rogue(female,not male); GUESTS: Nick Fury; SHIELD;";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());
		assertEquals(1, field.comicOrganizations.size());

		IRI xmen = assertHasOrganization(field.comicOrganizations, "X-Men");

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI nick = assertHasName(characters, "Nick Fury");
		IRI shield = assertHasName(characters, "SHIELD");

		ComicOrganization xmenOrg = (ComicOrganization) thingFactory.getCache()
				.get(xmen);

		assertEquals(2, xmenOrg.members.size());

		assertTrue(xmenOrg.members.contains(jean));
		assertTrue(xmenOrg.members.contains(rogue));

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<ComicCharacterNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof ComicCharacterNote) {
				notes.add((ComicCharacterNote) thing);
			}
		}

		assertEquals(4, notes.size());
		Iterator<ComicCharacterNote> it = notes.iterator();
		ComicCharacterNote storyNote1 = it.next();
		ComicCharacterNote storyNote2 = it.next();
		ComicCharacterNote storyNote3 = it.next();
		ComicCharacterNote storyNote4 = it.next();
		assertEquals("female", storyNote1.note.iterator().next());
		assertEquals("not male", storyNote2.note.iterator().next());
		assertEquals("GUEST", storyNote3.note.iterator().next());
		assertEquals("GUEST", storyNote4.note.iterator().next());

		assertTrue(storyNote3.comicCharacter.equals(nick)
				|| storyNote3.comicCharacter.equals(shield));

	}

	@Test
	public void teamAndStoryNote() throws Exception {
		String input = "X-Men--Jean Grey; Rogue; GUESTS: Nick Fury; SHIELD;";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());
		assertEquals(1, field.comicOrganizations.size());

		IRI xmen = assertHasOrganization(field.comicOrganizations, "X-Men");

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI nick = assertHasName(characters, "Nick Fury");
		IRI shield = assertHasName(characters, "SHIELD");

		ComicOrganization xmenOrg = (ComicOrganization) thingFactory.getCache()
				.get(xmen);

		assertEquals(2, xmenOrg.members.size());

		assertTrue(xmenOrg.members.contains(jean));
		assertTrue(xmenOrg.members.contains(rogue));

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<ComicCharacterNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof ComicCharacterNote) {
				notes.add((ComicCharacterNote) thing);
			}
		}

		assertEquals(2, notes.size());
		Iterator<ComicCharacterNote> it = notes.iterator();
		ComicCharacterNote storyNote1 = it.next();
		ComicCharacterNote storyNote2 = it.next();
		assertEquals("GUEST", storyNote1.note.iterator().next());
		assertEquals("GUEST", storyNote2.note.iterator().next());

		assertTrue(storyNote1.comicCharacter.equals(nick)
				|| storyNote1.comicCharacter.equals(shield));

	}

	@Test
	public void teamWithoutMembers() throws Exception {
		String input = "Legion";
		ThingFactory thingFactory = createThingFactory();

		OrgLookupService service = new OrgLookupService(
				Lists.newArrayList("Legion"));

		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, service, thingFactory, null);

		StoryTable.Fields.Character field = parser.parse(input);

		assertEquals(0, field.comicCharacters.size());
		assertEquals(1, field.comicOrganizations.size());
	}

	@Test
	public void twoStoryNotes() throws Exception {
		String input = "INTRODUCTION: Jean Grey; Rogue; GUESTS: Nick Fury; SHIELD;";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI nick = assertHasName(characters, "Nick Fury");
		IRI shield = assertHasName(characters, "SHIELD");

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<ComicCharacterNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof ComicCharacterNote) {
				notes.add((ComicCharacterNote) thing);
			}
		}

		assertEquals(4, notes.size());
		Iterator<ComicCharacterNote> it = notes.iterator();
		ComicCharacterNote storyNote1 = it.next();
		ComicCharacterNote storyNote2 = it.next();
		ComicCharacterNote storyNote3 = it.next();
		ComicCharacterNote storyNote4 = it.next();

		assertEquals("INTRODUCTION", storyNote1.note.iterator().next());
		assertEquals("INTRODUCTION", storyNote2.note.iterator().next());
		assertEquals("GUEST", storyNote3.note.iterator().next());
		assertEquals("GUEST", storyNote4.note.iterator().next());

		assertTrue(storyNote1.comicCharacter.equals(jean)
				|| storyNote1.comicCharacter.equals(rogue));

		assertTrue(storyNote3.comicCharacter.equals(nick)
				|| storyNote4.comicCharacter.equals(shield));

	}

	@Test
	public void twoStoryNotesAndLocalNote() throws Exception {
		String input = "INTRODUCTION: Jean Grey; Rogue; GUESTS: Nick Fury; SHIELD(is organization);";
		ThingFactory thingFactory = createThingFactory();

		ComicStory story = thingFactory.create(ComicStory.class);
		org.comicwiki.gcd.fields.CharacterFieldParser parser = createParser(
				input, new OrgLookupService(), thingFactory, story);

		StoryTable.Fields.Character field = parser.parse(input);

		Collection<ComicCharacter> characters = field.comicCharacters;

		assertEquals(4, characters.size());

		IRI jean = assertHasName(characters, "Jean Grey");
		IRI rogue = assertHasName(characters, "Rogue");
		IRI nick = assertHasName(characters, "Nick Fury");
		IRI shield = assertHasName(characters, "SHIELD");

		Collection<Thing> things = thingFactory.getCache().getThings();
		Collection<ComicCharacterNote> notes = Lists.newArrayList();
		for (Thing thing : things) {
			if (thing instanceof ComicCharacterNote) {
				notes.add((ComicCharacterNote) thing);
			}
		}

		assertEquals(5, notes.size());
		Iterator<ComicCharacterNote> it = notes.iterator();
		ComicCharacterNote storyNote1 = it.next();
		ComicCharacterNote storyNote2 = it.next();
		ComicCharacterNote storyNote3 = it.next();
		ComicCharacterNote storyNote4 = it.next();
		ComicCharacterNote storyNote5 = it.next();

		assertEquals("INTRODUCTION", storyNote1.note.iterator().next());
		assertEquals("INTRODUCTION", storyNote2.note.iterator().next());
		assertEquals("GUEST", storyNote3.note.iterator().next());
		assertEquals("GUEST", storyNote4.note.iterator().next());
		assertEquals("is organization", storyNote5.note.iterator().next());

		assertTrue(storyNote1.comicCharacter.equals(jean)
				|| storyNote1.comicCharacter.equals(rogue));

		assertTrue(storyNote3.comicCharacter.equals(nick)
				|| storyNote4.comicCharacter.equals(shield));

		assertTrue(storyNote5.comicCharacter.equals(shield));

	}

}
