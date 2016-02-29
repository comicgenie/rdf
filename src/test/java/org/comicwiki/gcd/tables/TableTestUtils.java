package org.comicwiki.gcd.tables;

import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;

public class TableTestUtils {

	public static StoryTable createStoryTable(ThingFactory thingFactory) {
		return new StoryTable(null, thingFactory, new IRICache(),
				new OrgLookupService());
	}
	
	public static SeriesTable createSeriesTable(ThingFactory thingFactory) {
		return new SeriesTable(null, thingFactory);
	}
	
	public static StoryTypeTable createStoryTypeTable(ThingFactory thingFactory) {
		return new StoryTypeTable(null);
	}
	
	public static ThingFactory createThingFactory() {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		return new ThingFactory(thingCache);
	}
	
}
