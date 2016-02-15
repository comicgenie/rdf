package org.comicwiki;

import static org.junit.Assert.*;

import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class ThingCacheTest {

	@Test
	public void add() throws Exception {
		Thing thing = new Thing();
		ThingCache.add(thing);
		
		assertEquals(1, ThingCache.sInstanceCache.size());
	}
}
