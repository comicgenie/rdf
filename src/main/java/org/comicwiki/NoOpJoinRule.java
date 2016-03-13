package org.comicwiki;

import java.util.List;

public class NoOpJoinRule implements JoinRule {

	@Override
	public boolean join(TableRow left, TableRow right) {
		return false;
	}

	@Override
	public void sort(List left, List right) {
		//noop
		
	}



}
