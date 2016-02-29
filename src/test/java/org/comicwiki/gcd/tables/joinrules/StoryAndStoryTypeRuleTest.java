package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.gcd.tables.StoryTypeTable;
import org.comicwiki.gcd.tables.StoryTypeTable.StoryTypeRow;
import org.junit.Test;

import static org.comicwiki.gcd.tables.TableTestUtils.*;
import static org.junit.Assert.assertEquals;

public class StoryAndStoryTypeRuleTest {

	@Test
	public void rule() throws Exception {	
		StoryTable table = createStoryTable(createThingFactory());
		StoryRow left = table.createRow();
		left.id = 1;
		left.fkTypeId = 6;
		
		StoryTypeRow right = new StoryTypeRow();
		right.name = "cover";
		right.id = 6;

		StoryAndStoryTypeRule rule = new StoryAndStoryTypeRule();
		rule.join(left, right);
		
		assertEquals("cover", left.storyType);
	}
}
