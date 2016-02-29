package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

public class StoryAndIssueRule implements
		JoinRule<StoryTable.StoryRow, IssueTable.IssueRow> {

	@Override
	public void join(StoryRow left, IssueRow right) {
		if (left.issueId == right.id) {
			left.editing.addAll(right.editors);
			left.indiciaPublisherId = right.indiciaPublisherId;
			left.seriesId = right.seriesId;
		}
	}
}
