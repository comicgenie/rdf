package org.comicwiki;

import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public final class ParserUtils {

	public static Collection<String> toCollection(List<TerminalNode> nodes) {
		Collection<String> items = Sets.newHashSet();

		for (TerminalNode ac : nodes) {
			String item = ac.getText();
			if (!Strings.isNullOrEmpty(item)) {
				items.add(item.trim());
			}
		}
		return items;
	}
}
