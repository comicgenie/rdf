package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.BrandGroupTable.BrandGroupRow;
import org.comicwiki.gcd.tables.BrandTable;
import org.comicwiki.gcd.tables.BrandTable.BrandRow;

public class BrandGroupAndBrandRule implements
		JoinRule<BrandGroupRow, BrandTable.BrandRow> {
	@Override
	public void join(BrandGroupRow left, BrandRow right) {
		if (left.fkBrandId == right.id) {
			left.brandInstanceID = right.instance.instanceId;
		}
	}
}
