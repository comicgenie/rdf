package org.comicwiki.gcd.tables.joinrules;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.comicwiki.BaseTable;
import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

public class StoryAndSeriesRule implements
		JoinRule<StoryTable.StoryRow, SeriesTable.SeriesRow> {

	@Override
	public boolean join(StoryRow left, SeriesRow right) {
		if (left.fkSeriesId == right.id) {
			left.series = right.instance;
			left.fkPublisherId = right.fkPublisherId;
			return true;
		}
		return false;
	}

	@Override
	public void sort(List<StoryRow> left, List<SeriesRow> right) {
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
		
		Collections.sort(left, new Comparator<StoryRow> () {

			@Override
			public int compare(StoryRow o1, StoryRow o2) {
				try {

					if (o1.fkSeriesId== o2.fkSeriesId) {
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
		
		
	}


}
