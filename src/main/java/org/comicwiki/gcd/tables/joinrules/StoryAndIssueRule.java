package org.comicwiki.gcd.tables.joinrules;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;
import org.comicwiki.model.schema.Person;

import com.google.common.collect.ObjectArrays;

public class StoryAndIssueRule implements
		JoinRule<StoryTable.StoryRow, IssueTable.IssueRow> {

	@Override
	public boolean join(StoryRow left, IssueRow right) {
		if (left.fkIssueId == right.id) {
			if (right.editors != null) {
				if (left.editing == null) {
					left.editing = right.editors;
				} else {
					left.editing = ObjectArrays.concat(left.editing,
							right.editors, Person.class);
				}
			}
			left.fkIndiciaPublisherId = right.fkIndiciaPublisherId;
			left.fkSeriesId = right.fkSeriesId;
			left.issue = right.instance;
			return true;
		}
		return false;
	}

	@Override
	public void sort(List<StoryRow> left, List<IssueRow> right) {
		Collections.sort(left, new Comparator<StoryRow>() {

			@Override
			public int compare(StoryRow o1, StoryRow o2) {
				try {

					if (o1.fkIssueId == o2.fkIssueId) {
						return 0;
					} else if (o1.fkIssueId < o2.fkIssueId) {
						return -1;
					} else {
						return 1;
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("Illegal compare values");
				}
			}

		});

		Collections.sort(right, new Comparator<IssueRow>() {

			@Override
			public int compare(IssueRow o1, IssueRow o2) {
				try {

					if (o1.id == o2.id) {
						return 0;
					} else if (o1.id < o2.id) {
						return -1;
					} else {
						return 1;
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("Illegal compare values");
				}
			}

		});

	}

}
