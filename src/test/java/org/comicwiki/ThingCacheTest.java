package org.comicwiki;

import static org.junit.Assert.*;

import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class ThingCacheTest {

	@Test
	public void add() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		Thing thing = new Thing();
		thingCache.add(thing);

		assertNotNull(thing.instanceId);
		assertEquals(1, thingCache.instanceCache.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void assignResourceIdsNoKey() throws Exception {
		IRICache iriCache = new IRICache();
		ResourceIDCache resourceIDCache = new ResourceIDCache();
		ThingCache thingCache = new ThingCache(new Repositories(),
				iriCache, resourceIDCache);
		
		Thing thing = new Thing();
		thingCache.add(thing);
		thingCache.assignResourceIDs();
	}
		
		
	@Test
	public void assignResourceIdsSameKey() throws Exception {
		IRICache iriCache = new IRICache();
		ResourceIDCache resourceIDCache = new ResourceIDCache();
		ThingCache thingCache = new ThingCache(new Repositories(),
				iriCache, resourceIDCache);
		
		Thing thing = new Thing();
		thing.name = "foo";
		
		Thing thing2 = new Thing();
		thing2.name = "foo2";
		thingCache.add(thing);
		thingCache.add(thing2);
		thingCache.assignResourceIDs();
		
		resourceIDCache.exportResourceIDs(null);
		
	}
	

}
