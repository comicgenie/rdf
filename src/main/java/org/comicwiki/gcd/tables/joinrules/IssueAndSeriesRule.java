package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class IssueAndSeriesRule implements
		JoinRule<IssueTable.IssueRow, SeriesTable.SeriesRow> {

	@Override
	public void join(IssueRow left, SeriesRow right) {
		if (left.fkSeriesId == right.id) {
			left.series = right.instance;
			left.fkPublisherId = right.fkPublisherId;
		}
	}
}
