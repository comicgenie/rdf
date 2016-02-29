package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

public class StoryAndSeriesRule implements
		JoinRule<StoryTable.StoryRow, SeriesTable.SeriesRow> {

	@Override
	public void join(StoryRow left, SeriesRow right) {
		if (left.seriesId == right.id) {
			left.seriesName = right.name;
			left.publisherId = right.publisherId;
		}
	}
}
