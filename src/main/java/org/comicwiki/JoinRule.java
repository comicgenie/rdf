package org.comicwiki;


public interface JoinRule<L extends TableRow<?>, R extends TableRow<?>> {

	void join(L left, R right );
}
