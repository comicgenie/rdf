package org.comicwiki.gcd.tables.joinrules;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.joinrules.LookupJoinRule;

public class StoryAndIssueRule implements
		LookupJoinRule<StoryTable.StoryRow,TIntObjectHashMap<IssueTable.IssueRow>> {

	@Override
	public boolean join(StoryRow storyRow, TIntObjectHashMap<IssueRow> map) {
		IssueRow issueRow = map.get(storyRow.fkIssueId);
		if (issueRow != null) {
			if (issueRow.editors != null) {
				if (storyRow.editing == null) {
					storyRow.editing = issueRow.editors;
				} else {
					//TODO: add and test
				//	storyRow.editing = ObjectArrays.concat(storyRow.editing,
				//			issueRow.editors, Person.class);
				}
			}
			storyRow.fkIndiciaPublisherId = issueRow.fkIndiciaPublisherId;
			storyRow.fkSeriesId = issueRow.fkSeriesId;
			storyRow.issue = issueRow.instance;
			return true;
		}

		return false;
	}

}
