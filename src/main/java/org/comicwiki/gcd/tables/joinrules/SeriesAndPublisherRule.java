package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.PublisherTable.PublisherRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class SeriesAndPublisherRule implements JoinRule<SeriesRow, PublisherRow> {

	@Override
	public void join(SeriesRow left, PublisherRow right) {
		if (left.fkPublisherId == right.id) {
			left.publisher = right.instance;
		}		
	}

}
