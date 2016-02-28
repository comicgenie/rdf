package org.comicwiki.gcd.tables;

import org.comicwiki.ThingFactory;

public class IssueTableTest extends TableTestCase<IssueTable>{

	@Override
	protected IssueTable createTable(ThingFactory thingFactory) {
		return new IssueTable(null);
	}

}
