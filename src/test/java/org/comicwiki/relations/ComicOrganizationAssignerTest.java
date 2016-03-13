package org.comicwiki.relations;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.comicwiki.IRICache;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ComicOrganizationAssignerTest {

	private ThingFactory factory;
	private ComicOrganization org;
	private ComicOrganizationAssigner assigner;

	@Before
	public void setUp() {
		factory = new ThingFactory(
				new ThingCache(new Repositories(new PersonNameMatcher()),
						new IRICache(), new ResourceIDCache()));
		org = factory.create(ComicOrganization.class);
		assigner = new ComicOrganizationAssigner(org);
	}

	@Test
	public void creators() throws Exception {
		Person p1Colorist = factory.create(Person.class);
		Person p2Inker = factory.create(Person.class);
		Person p3Letterer = factory.create(Person.class);
		Person p4Penciler = factory.create(Person.class);
		Person p5Script = factory.create(Person.class);
		Person p6Editor = factory.create(Person.class);

		Collection<Person> colors = Sets.newHashSet(p1Colorist);
		Collection<Person> inks = Sets.newHashSet(p2Inker);
		Collection<Person> letters = Sets.newHashSet(p3Letterer);
		Collection<Person> pencils = Sets.newHashSet(p4Penciler);
		Collection<Person> script = Sets.newHashSet(p5Script);
		Collection<Person> editors = Sets.newHashSet(p6Editor);

		assigner.creators(colors, inks, letters, pencils, script, editors);
		Stream.of(colors, inks, letters, pencils, script, editors)
				.flatMap(Collection::stream).forEach(e -> {
					assertTrue(e.workedOn.contains(org.instanceId));
				});
		assertTrue(org.creativeWork.colorists.contains(p1Colorist.instanceId));
		assertTrue(org.creativeWork.inkers.contains(p2Inker.instanceId));
		assertTrue(org.creativeWork.letterers.contains(p3Letterer.instanceId));
		assertTrue(org.creativeWork.pencilers.contains(p4Penciler.instanceId));
		assertTrue(org.creativeWork.authors.contains(p5Script.instanceId));
		assertTrue(org.creativeWork.editors.contains(p6Editor.instanceId));
	}

	@Test
	public void story() throws Exception {
		ComicStory story = factory.create(ComicStory.class);
		assigner.story(story);

		assertTrue(Arrays.asList(story.organizations).contains(org.instanceId));
	}

	@Test(expected = NullPointerException.class)
	public void nullInstanceId() throws Exception {
		new ComicOrganizationAssigner(new ComicOrganization());
	}
}
