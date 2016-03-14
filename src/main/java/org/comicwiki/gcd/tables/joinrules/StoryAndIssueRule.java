package org.comicwiki.gcd.tables.joinrules;

import java.util.Map;

import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.joinrules.LookupJoinRule;
import org.comicwiki.model.schema.Person;

import com.google.common.collect.ObjectArrays;

public class StoryAndIssueRule implements
		LookupJoinRule<StoryTable.StoryRow, Map<Integer, IssueTable.IssueRow>> {

	@Override
	public boolean join(StoryRow storyRow, Map<Integer, IssueRow> map) {
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
