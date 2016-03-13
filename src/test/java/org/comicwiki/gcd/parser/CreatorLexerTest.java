package org.comicwiki.gcd.parser;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class CreatorLexerTest {

	private static boolean match(Token source, String text, int type) {
		System.out.println(source);
		return source.getText().equals(text) && source.getType() == type;
	}

	@Test
	public void name() {
		String input = "Stan Lee";
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Stan Lee", CreatorLexer.WORD));
	}
	
	@Test
	public void twoNames() {
		String input = "Stan Lee;Kirby";
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Stan Lee", CreatorLexer.WORD));
		assertTrue(match(tokens.get(2), "Kirby", CreatorLexer.WORD));
	}
	
	
	@Test
	public void nameAndNote() {
		String input = "Stan Lee (a note)";
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Stan Lee", CreatorLexer.WORD));
		assertTrue(match(tokens.get(1), "a note", CreatorLexer.NOTES));
	}
	
	@Test
	public void nameAndAlias() {
		String input = "Stan Lee [as alias]";
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Stan Lee", CreatorLexer.WORD));
		assertTrue(match(tokens.get(1), "as alias", CreatorLexer.ALIAS));
	}
	
	@Test
	public void nameAndAliasAndNote() {
		String input = "Stan Lee [as alias] (a note)";
		CreatorLexer lexer = new CreatorLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Stan Lee", CreatorLexer.WORD));
		assertTrue(match(tokens.get(1), "as alias", CreatorLexer.ALIAS));
		assertTrue(match(tokens.get(3), "a note", CreatorLexer.NOTES));
	}
}
