package org.comicwiki.relations;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.IRI;
import org.comicwiki.IRICache;
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

public class ComicCreatorAssignerTest {

	Collection<Person> colors = new HashSet<>();
	Collection<Person> inks = new HashSet<>();
	Collection<Person> letters = new HashSet<>();
	Collection<Person> pencils = new HashSet<>();
	Collection<Person> script = new HashSet<>();
	Collection<Person> editors = new HashSet<>();
	
	private ThingFactory factory;
	private ComicCreatorAssigner cc;

	@Before
	public void setUp() {
		cc = new ComicCreatorAssigner(colors, inks, letters, pencils, script,
				editors);
		 factory = new ThingFactory(new ThingCache(
				new Repositories(), new IRICache(), new ResourceIDCache()));
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

	@Test
	public void colleagues() throws Exception {
		Person p1 = factory.create(Person.class);
		Person p2 = factory.create(Person.class);

		colors.add(p1);
		inks.add(p2);
			
		cc.colleagues();
		
		assertTrue(p1.colleagues.contains(p2.instanceId));
		assertTrue(p2.colleagues.contains(p1.instanceId));
		
		assertEquals(1, p1.colleagues.size());
		assertEquals(1, p2.colleagues.size());

	}
	
	@Test
	public void jobTitles() throws Exception {
		Person p1 = factory.create(Person.class);
		Person p2 = factory.create(Person.class);

		colors.add(p1);
		inks.add(p2);
			
		cc.jobTitles();
		
		assertTrue(p1.jobTitle.contains(CreatorRole.colorist.name()));
		assertTrue(p2.jobTitle.contains(CreatorRole.inker.name()));

	}
	
	@Test
	public void story() throws Exception {
		Person p1 = factory.create(Person.class);
		Person p2 = factory.create(Person.class);
	
		colors.add(p1);
		inks.add(p2);
			
		ComicStory story = factory.create(ComicStory.class);
		cc.story(story);
		
		assertTrue(story.colorists.contains(p1.instanceId));
		assertTrue(story.inkers.contains(p2.instanceId));
	}
	
	@Test
	public void character() throws Exception {
		Person p1 = factory.create(Person.class);
		Person p2 = factory.create(Person.class);
		
		colors.add(p1);
		inks.add(p2);
			
		ComicCharacter comicCharacter = factory.create(ComicCharacter.class);
		Collection<ComicCharacter> chars = new HashSet<>();
		chars.add(comicCharacter);
		cc.characters(chars);
		
		assertTrue(comicCharacter.creativeWork.colorists.contains(p1.instanceId));
		assertTrue(comicCharacter.creativeWork.inkers.contains(p2.instanceId));
	}
	
	@Test
	public void comicOrganization() throws Exception {
		Person p1 = factory.create(Person.class);
		Person p2 = factory.create(Person.class);
		
		colors.add(p1);
		inks.add(p2);
			
		ComicOrganization comicOrg = factory.create(ComicOrganization.class);
		Collection<ComicOrganization> orgs = new HashSet<>();
		orgs.add(comicOrg);
		cc.organizations(orgs);
		assertTrue(p1.workedOn.contains(comicOrg.instanceId));
		assertTrue(p2.workedOn.contains(comicOrg.instanceId));;
	}
}
