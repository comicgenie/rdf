package org.comicwiki.relations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.comicwiki.IRICache;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.schema.bib.ComicStory;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ComicCharacterAssignerTest {

	private ThingFactory factory;
	private ComicCharacter c1;
	private ComicCharacter c2;
	private ComicCharactersAssigner assigner;

	@Before
	public void setUp() {
		factory = new ThingFactory(
				new ThingCache(new Repositories(new PersonNameMatcher()),
						new IRICache(), new ResourceIDCache()));
		c1 = factory.create(ComicCharacter.class);
		c2 = factory.create(ComicCharacter.class);
		Collection<ComicCharacter> chars = Sets.newHashSet(c1, c2);

		assigner = new ComicCharactersAssigner(chars);

	}

	@Test
	public void colleagues() throws Exception {
		assigner.colleagues();

		assertTrue(Arrays.asList(c1.colleagues).contains(c2.instanceId));
		assertTrue(Arrays.asList(c2.colleagues).contains(c1.instanceId));

		assertEquals(1, c1.colleagues.length);
		assertEquals(1, c2.colleagues.length);
	}

	@Test(expected = NullPointerException.class)
	public void nullInstanceId() throws Exception {
		Collection<ComicCharacter> chars = Sets
				.newHashSet(new ComicCharacter());
		new ComicCharactersAssigner(chars);
	}

	@Test
	public void story() throws Exception {
		ComicStory story = factory.create(ComicStory.class);
		assigner.story(story);

		assertTrue(Arrays.asList(story.characters).contains(c1.instanceId));
		assertTrue(Arrays.asList(story.characters).contains(c2.instanceId));
	}
}
