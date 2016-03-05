package org.comicwiki.gcd.tables.joinrules;


import static org.junit.Assert.*;

import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.junit.Test;
import static org.comicwiki.gcd.tables.TableTestUtils.*;

public class StoryAndSeriesRuleTest {

	@Test
	public void rule() throws Exception {	
		StoryTable table = createStoryTable(createThingFactory());
		StoryRow left = new StoryRow();
		left.id = 1;
		left.fkSeriesId = 10;
		
		SeriesRow right = createSeriesTable(createThingFactory()).createRow();
		right.instance.name = "Amazing Spiderman";
		right.id = 10;

		StoryAndSeriesRule rule = new StoryAndSeriesRule();
		rule.join(left, right);
		
		assertEquals("Amazing Spiderman", left.series.name);
	}
	

}
