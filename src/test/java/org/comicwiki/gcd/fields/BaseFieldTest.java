package org.comicwiki.gcd.fields;

import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;

public class BaseFieldTest {

	protected ThingFactory createThingFactory() {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		return new ThingFactory(thingCache);
	}
}
