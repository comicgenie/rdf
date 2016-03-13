package org.comicwiki;

import java.lang.reflect.Field;

public class FieldJoinRule implements JoinRule {

	private Field fk;
	private Field rk;
	private Field lf;
	private Field rf;

	public FieldJoinRule(Class left, Class right, String leftKey, String rightKey, String leftField,
			String rightField) throws Exception {
		fk = left.getDeclaredField(leftKey);
		rk = right.getDeclaredField(rightKey);
		lf = left.getDeclaredField(leftField);
		rf = right.getDeclaredField(rightField);
	}

	@Override
	public boolean join(TableRow left, TableRow right) {
		try {
			if (fk.getInt(left) == rk.getInt(right)) {
				lf.set(left, rf.get(right));
				return true;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
		
	}
}
