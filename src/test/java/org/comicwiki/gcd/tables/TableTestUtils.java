package org.comicwiki.gcd.tables;

import org.comicwiki.IRICache;
import org.comicwiki.OrgLookupService;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.fields.FieldParserFactory;

public class TableTestUtils {

	public static SeriesTable createSeriesTable(ThingFactory thingFactory) {
		return new SeriesTable(null, thingFactory, new FieldParserFactory(
				thingFactory));
	}

	public static StoryTable createStoryTable(ThingFactory thingFactory) {
		return new StoryTable(null, thingFactory, new FieldParserFactory(
				thingFactory), new OrgLookupService());
	}

	public static StoryTypeTable createStoryTypeTable(ThingFactory thingFactory) {
		return new StoryTypeTable(null);
	}

	public static ThingFactory createThingFactory() {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());
		return new ThingFactory(thingCache);
	}

}
