package org.comicwiki.gcd.tables.joinrules;

import org.comicwiki.JoinRule;
import org.comicwiki.gcd.tables.IssueReprintTable.IssueReprintRow;
import org.comicwiki.gcd.tables.IssueTable.IssueRow;
import org.comicwiki.model.ReprintNote;
import org.comicwiki.model.schema.bib.ComicIssue;

public class IssueReprintRule implements JoinRule<IssueRow, IssueReprintRow> {

	@Override
	public void join(IssueRow left, IssueReprintRow right) {
		ReprintNote reprintNote = right.instance;

		if (left.id == right.fkOriginIssueId) {
			ComicIssue original = left.instance;
			original.reprintNote.add(reprintNote.instanceId);
		} else if (left.id == right.fkTargetIssueId) {
			ComicIssue reprint = left.instance;
			reprint.reprintNote.add(reprintNote.instanceId);
		}
	}
}
