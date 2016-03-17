package org.comicwiki.gcd.tables.joinrules;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.joinrules.LookupJoinRule;

public class StoryAndSeriesRule implements
		LookupJoinRule<StoryTable.StoryRow, TIntObjectHashMap<SeriesTable.SeriesRow>> {

	@Override
	public boolean join(StoryRow storyRow, TIntObjectHashMap<SeriesTable.SeriesRow> map) {
		SeriesRow seriesRow = map.get(storyRow.fkSeriesId);
		if(seriesRow != null) {
			storyRow.series = seriesRow.instance;
			storyRow.fkPublisherId = seriesRow.fkPublisherId;
			return true;
		}
		return false;
	}
}
