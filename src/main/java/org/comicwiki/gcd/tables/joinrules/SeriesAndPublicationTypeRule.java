package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.SeriesPublicationTypeTable.SeriesPublicationTypeRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class SeriesAndPublicationTypeRule implements
		JoinRule<SeriesRow, SeriesPublicationTypeRow> {

	@Override
	public void join(SeriesRow left, SeriesPublicationTypeRow right) {
		if (left.fkPublicationTypeId == right.id) {
			left.publicationType = right.name;
		}
	}
}
