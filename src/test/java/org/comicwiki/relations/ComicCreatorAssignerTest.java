package org.comicwiki.relations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.IRICache;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.CreatorRole;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ComicCreatorAssignerTest {

	// private ComicCreatorAssigner assigner;
	Collection<Person> colors = new HashSet<>();
	Collection<Person> editors = new HashSet<>();
	private ThingFactory factory;
	Collection<Person> inks = new HashSet<>();
	Collection<Person> letters = new HashSet<>();

	private Person p1Colorist;
	private Person p2Inker;
	private Person p3Letterer;
	private Person p4Penciler;
	private Person p5Script;
	private Person p6Editor;
	Collection<Person> pencils = new HashSet<>();
	Collection<Person> script = new HashSet<>();

	private ComicCreatorAssigner assigner() {
		return new ComicCreatorAssigner(colors, inks, letters, pencils, script,
				editors);
	}

	@Test(expected = NullPointerException.class)
	public void nullInstanceId() throws Exception {
		colors.add(new Person());
		new ComicCreatorAssigner(colors, inks, letters, pencils, script,
				editors);
	}

	@Test
	public void character() throws Exception {
		ComicCreatorAssigner assigner = assigner();
		ComicCharacter comicCharacter = factory.create(ComicCharacter.class);
		assigner.characters(Sets.newHashSet(comicCharacter));

		assertTrue(comicCharacter.creativeWork.colorists
				.contains(p1Colorist.instanceId));
		assertTrue(comicCharacter.creativeWork.inkers
				.contains(p2Inker.instanceId));
	}

	@Test(expected = NullPointerException.class)
	public void noCharacterInstanceId() throws Exception {
		ComicCreatorAssigner assigner = new ComicCreatorAssigner(colors, inks,
				letters, pencils, script, editors);
		assigner.characters(Sets.newHashSet(new ComicCharacter()));
	}

	@Test
	public void colleagues() throws Exception {
		ComicCreatorAssigner assigner = assigner();
		assigner.colleagues();

		assertTrue(p1Colorist.colleagues.contains(p2Inker.instanceId));
		assertTrue(p2Inker.colleagues.contains(p1Colorist.instanceId));
		assertTrue(p3Letterer.colleagues.contains(p1Colorist.instanceId));
		assertFalse(p3Letterer.colleagues.contains(p3Letterer.instanceId));

		assertEquals(5, p1Colorist.colleagues.size());
		assertEquals(5, p2Inker.colleagues.size());
		assertEquals(5, p3Letterer.colleagues.size());
		assertEquals(5, p3Letterer.colleagues.size());
		assertEquals(5, p5Script.colleagues.size());
		assertEquals(5, p6Editor.colleagues.size());

	}

	@Test
	public void comicOrganization() throws Exception {
		ComicCreatorAssigner assigner = assigner();

		ComicOrganization comicOrg = factory.create(ComicOrganization.class);
		assigner.comicOrganizations(Sets.newHashSet(comicOrg));
		assertTrue(p1Colorist.workedOn.contains(comicOrg.instanceId));
		assertTrue(p2Inker.workedOn.contains(comicOrg.instanceId));
	}

	@Test
	public void jobTitles() throws Exception {
		ComicCreatorAssigner assigner = assigner();
		assigner.jobTitles();
		assertTrue(p1Colorist.jobTitle.contains(CreatorRole.colorist.name()));
		assertTrue(p2Inker.jobTitle.contains(CreatorRole.inker.name()));
		assertTrue(p3Letterer.jobTitle.contains(CreatorRole.letterist.name()));
		assertTrue(p4Penciler.jobTitle.contains(CreatorRole.penciller.name()));
		assertTrue(p5Script.jobTitle.contains(CreatorRole.writer.name()));
		assertTrue(p6Editor.jobTitle.contains(CreatorRole.editor.name()));
	}

	@Before
	public void setUp() {
		factory = new ThingFactory(
				new ThingCache(new Repositories(new PersonNameMatcher()),
						new IRICache(), new ResourceIDCache()));
		p1Colorist = factory.create(Person.class);
		p2Inker = factory.create(Person.class);
		p3Letterer = factory.create(Person.class);
		p4Penciler = factory.create(Person.class);
		p5Script = factory.create(Person.class);
		p6Editor = factory.create(Person.class);

		colors.add(p1Colorist);
		inks.add(p2Inker);
		letters.add(p3Letterer);
		pencils.add(p4Penciler);
		script.add(p5Script);
		editors.add(p6Editor);
	}

	@Test
	public void story() throws Exception {
		ComicCreatorAssigner assigner = assigner();
		ComicStory story = factory.create(ComicStory.class);
		assigner.story(story);

		assertTrue(story.colorists.contains(p1Colorist.instanceId));
		assertTrue(story.inkers.contains(p2Inker.instanceId));
	}

	@After
	public void tearDown() {
		colors.clear();
		inks.clear();
		letters.clear();
		pencils.clear();
		script.clear();
		editors.clear();

	}
}
