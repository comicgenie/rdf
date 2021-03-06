package org.comicwiki.gcd.tables;

import org.comicwiki.BaseTable;
import org.comicwiki.IRICache;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.junit.Test;

public abstract class TableTestCase<T extends BaseTable> {

	protected ThingFactory createThingFactory() {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());
		return new ThingFactory(thingCache);
	}

	protected abstract T createTable(ThingFactory thingFactory);

	protected T createTable() {
		return createTable(createThingFactory());
	}

	@Test(expected = IllegalArgumentException.class)
	public void badJcbcUrl() throws Exception {
		T table = createTable();
		table.saveToParquetFormat("jdb:mysql://invalid");
	}
}
