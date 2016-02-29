package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.BrandEmblemGroupTable.BrandEmblemGroupRow;
import org.comicwiki.gcd.tables.BrandGroupTable.BrandGroupRow;

public final class BrandGroupAndBrandEmblemRule implements
		JoinRule<BrandGroupRow, BrandEmblemGroupRow> {

	@Override
	public void join(BrandGroupRow left, BrandEmblemGroupRow right) {
		if (left.id == right.brandGroupId) {
			left.fkBrandId = right.brandId;
		}
	}

}
