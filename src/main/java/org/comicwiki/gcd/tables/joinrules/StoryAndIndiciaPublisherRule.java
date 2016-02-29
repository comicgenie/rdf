package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IndiciaPublisherTable;
import org.comicwiki.gcd.tables.IndiciaPublisherTable.IndiciaPublisherRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

public class StoryAndIndiciaPublisherRule
		implements
		JoinRule<StoryTable.StoryRow, IndiciaPublisherTable.IndiciaPublisherRow> {

	@Override
	public void join(StoryRow left, IndiciaPublisherRow right) {
		if (left.fkIndiciaPublisherId == right.id) {
			left.indiciaPublisher = right.instance;
		}
	}
}
