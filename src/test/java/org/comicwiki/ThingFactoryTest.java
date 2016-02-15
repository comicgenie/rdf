package org.comicwiki;


import static org.junit.Assert.*;

import org.comicwiki.model.ComicCharacter;
import org.junit.Test;

public class ThingFactoryTest {

	@Test
	public void testCreateHasInstanceId() {
		ComicCharacter thing = ThingFactory.create(ComicCharacter.class);
		assertNotNull(thing.instanceId);
	}
}
