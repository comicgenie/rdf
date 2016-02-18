package org.comicwiki;

import static org.junit.Assert.assertEquals;

import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class RepositoryTest {

	@Test
	public void testMergeStringField() throws Exception {
		Repository<Thing> thingRepo = new Repository<Thing>();
		Thing c1 = new Thing();
		c1.name = "Superman";
		Thing c2 = new Thing();

		thingRepo.merge(c1, c2);
		assertEquals("Superman", c2.name);
	}
	
	@Test
	public void testMergeNoOverrideStringField() throws Exception {
		Repository<Thing> thingRepo = new Repository<Thing>();
		Thing c1 = new Thing();
		c1.name = "Superman";
		Thing c2 = new Thing();
		c1.name = "Superman 2";

		thingRepo.merge(c1, c2);
		assertEquals("Superman 2", c2.name);
	}
}
