package org.comicwiki.gcd.tables.joinrules;


import static org.comicwiki.gcd.tables.TableTestUtils.createSeriesTable;
import static org.comicwiki.gcd.tables.TableTestUtils.createStoryTable;
import static org.comicwiki.gcd.tables.TableTestUtils.createThingFactory;
import static org.junit.Assert.assertEquals;

import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.junit.Test;

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
