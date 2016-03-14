package org.comicwiki.joinrules;

import java.util.Map;

import org.comicwiki.TableRow;

public interface LookupJoinRule<L extends TableRow<?>, M extends Map<Integer, ? extends TableRow<?>>> extends JoinRule {

	boolean join(L left, M map);

}
