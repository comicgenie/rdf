package org.comicwiki;

import java.util.List;


public interface JoinRule<L extends TableRow<?>, R extends TableRow<?>> {

	boolean join(L left, R right );
	
	void sort(List<L> left, List<R> right);
}
