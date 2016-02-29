package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.gcd.tables.StoryTypeTable;
import org.comicwiki.gcd.tables.StoryTypeTable.StoryTypeRow;

public class StoryAndStoryTypeRule implements
		JoinRule<StoryTable.StoryRow, StoryTypeTable.StoryTypeRow> {

	@Override
	public void join(StoryRow left, StoryTypeRow right) {
		if (left.typeId == right.id) {
			left.storyType = right.name;
		}
	}
}
