package org.comicwiki.joinrules;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.comicwiki.TableRow;

public interface LookupJoinRule<L extends TableRow<?>, M extends TIntObjectHashMap<? extends TableRow<?>>> extends JoinRule {

	boolean join(L left, M map);

}
