package org.comicwiki;

import static org.junit.Assert.*;

import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class ThingCacheTest {

	@Test
	public void add() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories());
		Thing thing = new Thing();
		thingCache.add(thing);
		
		assertEquals(1, thingCache.instanceCache.size());
	}
}
