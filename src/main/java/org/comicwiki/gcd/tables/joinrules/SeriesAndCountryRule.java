package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.CountryTable.CountryRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class SeriesAndCountryRule implements JoinRule<SeriesRow, CountryRow> {

	@Override
	public void join(SeriesRow left, CountryRow right) {
		if (left.fkCountryId == right.id) {
			left.country = right.instance;
		}
	}
}
