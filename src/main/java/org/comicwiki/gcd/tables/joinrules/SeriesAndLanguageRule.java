package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.LanguageTable.LanguageRow;
import org.comicwiki.gcd.tables.SeriesTable.SeriesRow;

public class SeriesAndLanguageRule implements JoinRule<SeriesRow, LanguageRow> {

	@Override
	public void join(SeriesRow left, LanguageRow right) {
		if (left.fkLanguageId == right.id) {
			left.language = right.instance;
		}
	}
}
