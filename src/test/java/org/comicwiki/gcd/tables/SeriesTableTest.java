package org.comicwiki.gcd.tables;

import org.comicwiki.ThingFactory;

public class SeriesTableTest extends TableTestCase<SeriesTable>{

	@Override
	protected SeriesTable createTable(ThingFactory thingFactory) {
		return new SeriesTable(null, thingFactory);
	}

}
