package org.comicwiki.gcd.parser;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class PriceFieldLexerTest {

	private static boolean match(Token source, String text, int type) {
		System.out.println(source);
		return source.getText().equals(text) && source.getType() == type;
	}

	@Test
	public void nondecimal() {
		String input = "2/6 [0-2-6 GBP]";
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "2/6", PriceLexer.FRACTION));
		assertTrue(match(tokens.get(2), "0-2-6", PriceLexer.BRITISH));
		assertTrue(match(tokens.get(3), "GBP", PriceLexer.CURRENCY_CODE));
	}
	@Test
	public void euro() {
		String input = "3.99 EUR DE";
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "3.99", PriceLexer.PRICE));
		assertTrue(match(tokens.get(1), "EUR", PriceLexer.CURRENCY_CODE));
		assertTrue(match(tokens.get(2), "DE", PriceLexer.COUNTRY));
	}
	
	@Test
	public void simple() {
		String input = "1.99 USD";
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "1.99", PriceLexer.PRICE));
		assertTrue(match(tokens.get(1), "USD", PriceLexer.CURRENCY_CODE));
	}
	
	@Test
	public void inferred() {
		String input = "[2.50 CAD]";
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(1), "2.50", PriceLexer.PRICE));
		assertTrue(match(tokens.get(2), "CAD", PriceLexer.CURRENCY_CODE));
	}
	
	@Test
	public void inferredWithNote() {
		String input = "[2.50 CAD] (see notes)";
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(
				input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(1), "2.50", PriceLexer.PRICE));
		assertTrue(match(tokens.get(2), "CAD", PriceLexer.CURRENCY_CODE));
		assertTrue(match(tokens.get(4), "see notes", PriceLexer.NOTE));
	}
}
