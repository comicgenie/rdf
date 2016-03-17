package org.comicwiki.gcd.tables.joinrules;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.joinrules.LookupJoinRule;

public class IssueAndSeriesRule implements
		LookupJoinRule<IssueTable.IssueRow, TIntObjectHashMap<SeriesRow>> {

	@Override
	public boolean join(IssueRow issueRow, TIntObjectHashMap<SeriesRow> map) {
		SeriesRow seriesRow = map.get(issueRow.fkSeriesId);
		if(seriesRow != null) {
			issueRow.series = seriesRow.instance;
			issueRow.fkPublisherId = seriesRow.fkPublisherId;
			return true;
		}
		return false;
	}
}
