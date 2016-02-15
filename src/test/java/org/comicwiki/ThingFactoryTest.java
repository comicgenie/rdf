package org.comicwiki;


import static org.junit.Assert.*;

import org.comicwiki.model.ComicCharacter;
import org.junit.Test;

public class ThingFactoryTest {

	@Test
	public void testCreateHasInstanceId() {
		ThingCache thingCache = new ThingCache();
		ComicCharacter thing = new ThingFactory(thingCache).create(ComicCharacter.class);
		assertNotNull(thing.instanceId);
	}
}
