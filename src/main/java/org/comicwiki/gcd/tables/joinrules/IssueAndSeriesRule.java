package org.comicwiki.gcd.tables.joinrules;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.comicwiki.BaseTable;
import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class IssueAndSeriesRule implements
		JoinRule<IssueTable.IssueRow, SeriesTable.SeriesRow> {

	@Override
	public boolean join(IssueRow left, SeriesRow right) {
		if (left.fkSeriesId == right.id) {
			left.series = right.instance;
			left.fkPublisherId = right.fkPublisherId;
			return true;
		}
		return false;
	}

	@Override
	public void sort(List<IssueRow> left, List<SeriesRow> right) {
		Collections.sort(left, new Comparator<IssueRow> () {

			@Override
			public int compare(IssueRow o1, IssueRow o2) {
				try {

					if (o1.fkSeriesId == o2.fkSeriesId) {
						return 0;
					} else if (o1.fkSeriesId < o2.fkSeriesId) {
						return -1;
					} else {
						return 1;
					}
				} catch (Exception e) {
					throw new IllegalArgumentException(
							"Illegal compare values");
				}
			}
			
		});
		
		Collections.sort(right, new Comparator<SeriesRow> () {

			@Override
			public int compare(SeriesRow o1, SeriesRow o2) {
				try {

					if (o1.id == o2.id) {
						return 0;
					} else if (o1.id < o2.id) {
						return -1;
					} else {
						return 1;
					}
				} catch (Exception e) {
					throw new IllegalArgumentException(
							"Illegal compare values");
				}
			}
			
		});
	}
}
