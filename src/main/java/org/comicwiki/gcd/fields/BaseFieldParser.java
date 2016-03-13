package org.comicwiki.gcd.fields;

import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseFieldParser {

	protected String getValue(TerminalNode node) {
		return node == null ? null : node.getText();
	}

}
