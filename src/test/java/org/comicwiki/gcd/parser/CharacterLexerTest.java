package org.comicwiki.gcd.parser;

import static org.junit.Assert.*;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.comicwiki.gcd.parser.CharacterFieldParser.CharactersContext;
import org.junit.Test;

public class CharacterLexerTest {
//
//FEATURE: Fantastic Four [Mr. Fantastic; Invisible Girl; Human Torch [Johnny Storm]; The Thing]; GUESTS: Crystal; Lockjaw; VILLAIN: Diablo
//* X-Men--Jean Grey; Rogue; Beast; Nightcrawler; Kitty Pryde; GUESTS: Nick Fury; SHIELD;
	@Test
	public void lex6() {
		String input ="FEATURE: Fantastic Four [Mr. Fantastic; Invisible Girl; Human Torch [Johnny Storm]; The Thing]; GUESTS: Crystal; Lockjaw; VILLAIN: Diablo";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		
		CharacterFieldParser parser = new CharacterFieldParser(new CommonTokenStream(lexer));
		CharactersContext ctx = parser.characters();
		System.out.println(ctx.getText());
	
	}
	/**
	 * 		CharacterFieldParser parser = new CharacterFieldParser(new CommonTokenStream(lexer));
		CharactersContext ctx = parser.characters();
		System.out.println(ctx.getText());
	 */
	private static boolean match(Token source, String text, int type) {
		System.out.println(source);
		return source.getText().equals(text) && source.getType() == type;
	}
	
	
	/////
	@Test
	public void simple() {
		String input ="Mr. Fantastic; Invisible Girl";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Mr. Fantastic", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "Invisible Girl", CharacterFieldLexer.WORD));
	}


	@Test
	public void  noteInAlias() {
		String input = "Mr. Fantastic [X(note)]";;
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Mr. Fantastic", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "X", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "note", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void simpleOrganization() {
		String input ="Fantastic Four [Mr. Fantastic; Invisible Girl]";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Fantastic Four", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "Mr. Fantastic", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "Invisible Girl", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void twoAltOrganizations() {
		String input ="X-Men<EM_DASH>Jean Grey; Rogue;Y-Men<EM_DASH>A;B";;
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "X-Men", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(1), "<EM_DASH>", CharacterFieldLexer.ORGANIZATION_MARKER));
		assertTrue(match(tokens.get(2), "Jean Grey", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "Rogue", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(6), "Y-Men", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(7), "<EM_DASH>", CharacterFieldLexer.ORGANIZATION_MARKER));
		assertTrue(match(tokens.get(8), "A", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void complexOrganization() {
		String input ="Fantastic Four [Mr. Fantastic; Invisible Girl; Human Torch [Johnny Storm]; The Thing]";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Fantastic Four", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "Mr. Fantastic", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "Invisible Girl", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(6), "Human Torch", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(8), "Johnny Storm", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(11), "The Thing", CharacterFieldLexer.WORD));
	}
	@Test
	public void complexOrganizationNoSpaces() {
		String input ="Fantastic Four[Mr. Fantastic;Invisible Girl;Human Torch[Johnny Storm];The Thing]";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "Fantastic Four", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "Mr. Fantastic", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "Invisible Girl", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(6), "Human Torch", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(8), "Johnny Storm", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(11), "The Thing", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void mixedBracketsAndParenthesis() {
		String input ="camp kids [Richard (Richie, boy in wheelchair); Timmy; Tony; and others];";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "camp kids", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(2), "Richard", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "Richie", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(6), "boy in wheelchair", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(9), "Timmy", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(11), "Tony", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void organizationMarker() {
		String input ="X-Men<EM_DASH>Kitty Pryde; GUESTS: Nick Fury; SHIELD";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "X-Men", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(1), "<EM_DASH>", CharacterFieldLexer.ORGANIZATION_MARKER));
		assertTrue(match(tokens.get(2), "Kitty Pryde", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "GUEST", CharacterFieldLexer.GUEST));
		assertTrue(match(tokens.get(5), "Nick Fury", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(7), "SHIELD", CharacterFieldLexer.WORD));
	}	
	
	@Test
	public void organizationMarkerNoSpaces() {
		String input ="X-Men<EM_DASH>Kitty Pryde;GUESTS:Nick Fury;SHIELD";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "X-Men", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(1), "<EM_DASH>", CharacterFieldLexer.ORGANIZATION_MARKER));
		assertTrue(match(tokens.get(2), "Kitty Pryde", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(4), "GUEST", CharacterFieldLexer.GUEST));
		assertTrue(match(tokens.get(5), "Nick Fury", CharacterFieldLexer.WORD));
		assertTrue(match(tokens.get(7), "SHIELD", CharacterFieldLexer.WORD));
	}	
	
	@Test
	public void simpleNotes() {
		String input ="GUESTS:Nick Fury";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "GUEST", CharacterFieldLexer.GUEST));
		assertTrue(match(tokens.get(1), "Nick Fury", CharacterFieldLexer.WORD));
	}	
	
	@Test
	public void simpleNotesCASE() {
		String input ="Guests:Nick Fury";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "GUEST", CharacterFieldLexer.GUEST));
		assertTrue(match(tokens.get(1), "Nick Fury", CharacterFieldLexer.WORD));
	}	
	
	@Test
	public void simpleNotesGuestAbbreviated() {
		String input ="G:Nick Fury";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "GUEST", CharacterFieldLexer.GUEST));
		assertTrue(match(tokens.get(1), "Nick Fury", CharacterFieldLexer.WORD));
	}
	
	@Test
	public void flashback() {
		String input ="Cameo Flashback:Nick Fury";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		List<? extends Token> tokens = lexer.getAllTokens();
		assertTrue(match(tokens.get(0), "CAMEO FLASHBACK", CharacterFieldLexer.CAMEO_FLASHBACK));
	}	

}
