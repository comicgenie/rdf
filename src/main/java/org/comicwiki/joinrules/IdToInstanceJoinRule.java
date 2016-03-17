package org.comicwiki.joinrules;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.lang.reflect.Field;

import org.comicwiki.TableRow;
import org.comicwiki.model.schema.Thing;

public class IdToInstanceJoinRule implements JoinRule {

	public boolean join(TableRow<? extends Thing> leftRow,
			TIntObjectHashMap<? extends TableRow<?>> map, Field foreignKey,
			Field targetField) {
		try {
			Integer fkInt = (Integer) foreignKey.get(leftRow);
			TableRow<? extends Thing> rightRow = map.get(fkInt);
			if (rightRow != null) {
				targetField.set(leftRow, rightRow.instance);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
