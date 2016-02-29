package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.PublisherTable;
import org.comicwiki.gcd.tables.PublisherTable.PublisherRow;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

public class StoryAndPublisherRule implements
	JoinRule<StoryTable.StoryRow, PublisherTable.PublisherRow> {

	@Override
	public void join(StoryRow left, PublisherRow right) {
		if(left.fkPublisherId == right.id) {
			left.publisher = right.instance;
		}	
	} 
}
