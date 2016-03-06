package org.comicwiki.gcd.parser;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class IssueNumberLexerTest {

	private static boolean match(Token source, String text, int type) {
		System.out.println(source);
		return source.getText().equals(text) && source.getType() == type;
	}

	@Test
	public void one() {
		String input = "1";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "1", IssueNumberLexer.ISSUE_NUMBER));
	}
	
	@Test
	public void year() {
		String input = "5/2016";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "5", IssueNumberLexer.ISSUE_NUMBER));
		assertTrue(match(tokens.get(2), "2016", IssueNumberLexer.YEAR));
	}
	
	@Test
	public void yearWithNote() {
		String input = "5/2016 (#234)";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "5", IssueNumberLexer.ISSUE_NUMBER));
		assertTrue(match(tokens.get(2), "2016", IssueNumberLexer.YEAR));
		assertTrue(match(tokens.get(3), "#234", IssueNumberLexer.COVER));
	}
	
	@Test
	public void indicia() throws Exception {
		String input = "34 (132)";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "34", IssueNumberLexer.ISSUE_NUMBER));
		assertTrue(match(tokens.get(1), "132", IssueNumberLexer.COVER));
	}
	
	@Test
	public void assigned() throws Exception {
		String input = "34 [132]";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "34", IssueNumberLexer.ISSUE_NUMBER));
		assertTrue(match(tokens.get(1), "132", IssueNumberLexer.ASSIGNED));
	}
	
	@Test
	public void assignedOnly() throws Exception {
		String input = "[132]";
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "132", IssueNumberLexer.ASSIGNED));
	}
}
