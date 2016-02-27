/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki.gcd;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.test.runtime.java.BaseTest;
import org.comicwiki.IRICache;
import org.comicwiki.Repositories;
import org.comicwiki.ResourceIDCache;
import org.comicwiki.ThingCache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CharacterFieldLexer;
import org.comicwiki.gcd.parser.CharacterFieldParser;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CharacterFieldWalkerTest extends BaseTest {

	private File resourceDir = new File("./src/main/resources/comics");

	private CharacterFieldParser createParser(String text) {
		text = CharacterFieldCleaner.cleanSemicolonInParanthesis(text);
		System.out.println(text);
		text = CharacterFieldCleaner.cleanCommaInBrackets(text);
		System.out.println(text);
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterFieldParser parser = new CharacterFieldParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());
		return parser;
	}

	private CharacterFieldWalker walk(CharacterFieldParser parser) {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTreeWalker walker = new ParseTreeWalker();
		CharacterFieldWalker cWalker = new CharacterFieldWalker(thingFactory,
				characterCreator, new ComicOrganizationsLookupService());
		walker.walk(cWalker, parser.characters());
		return cWalker;
	}

	/**
	 * This is not according to spec. Only Parenthesis
	 */
	/*
	 * @Test public void testOnlyParenthesis() throws IOException { String text
	 * = "(woman on ice skates)"; CharacterFieldParser parser = createParser(text);
	 * walk(parser); }
	 */
	@Test
	public void testMismatch() throws IOException {
		String text = "Batman [Bruce Wayne]; Robin [Dick Grayson]; John Barham (mention only;death); James Barham (millionaire;death); Adam Barham (Jame's cousin); John Gorley; Sheriff Martin; Robert Cray (villain;introduction); Vance Sonderson [as Jay Sonderson] (villain,gun smuggler,introduction)";
		CharacterFieldParser parser = createParser(text);
		CharacterFieldWalker cw = walk(parser);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, cw.characters);
		mapper.writeValue(System.out, cw.organizations);

		System.out.println(cw.errors);
	}

	/**
	 * This is not according to spec. Semicolon instead of comma
	 */
	@Test
	public void testSemicolonInParenthesis() throws IOException {
		String text = "Wilfred of Ivanhoe (introduction; not named,referred to as \"a stranger\")";
		CharacterFieldParser parser = createParser(text);
		walk(parser);
	}

	@Test
	public void testB() throws IOException {
		String text = "Batman; Robin; The Thing";
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterFieldParser parser = new CharacterFieldParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterFieldWalker(thingFactory, characterCreator,
				new ComicOrganizationsLookupService()), parser.characters());
	}

	//
	@Test
	public void testC() throws IOException {
		String fields = "Batman; Robin*(batman's sidekick); The Thing [Ben; MyThing](a member of F4, badass)";
		//String input ="Fantastic Four [Mr. Fantastic(sample,sample2); Invisible Girl; Human Torch [Johnny Storm]; The Thing]";
		String input ="X-Men<EM_DASH>Wolverine; FEATURE: Fantastic Four [Mr. Fantastic; Invisible Girl; Human Torch [Johnny Storm]; The Thing]; GUESTS: Crystal; Lockjaw; VILLAIN: Diablo (a,b)X-Men2<EM_DASH>Wolverine2; ";
		//	String fields = "ABC (,hello world, hello universe)";
	//	ComicOrganizationsLookupService service = ComicOrganizationsLookupService(Lists.ne);
		
		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterFieldParser parser = new CharacterFieldParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.root();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterFieldWalker(thingFactory, characterCreator,
				new ComicOrganizationsLookupService()), tree);
	}

	@Test
	public void testE() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";

		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(text2));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterFieldParser parser = new CharacterFieldParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.characters();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterFieldWalker(thingFactory, characterCreator,
				new ComicOrganizationsLookupService()), tree);
	}

	@Test
	public void testF() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";
		String text3 = "X-Men<DOUBLE_HYPHEN>Jean Grey; Rogue";

		CharacterFieldLexer lexer = new CharacterFieldLexer(new ANTLRInputStream(text3));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterFieldParser parser = new CharacterFieldParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.characters();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterFieldWalker(thingFactory, characterCreator,
				new ComicOrganizationsLookupService()), tree);
	}

	@Test
	public void testTeam() throws IOException {
		String text = "Justice League of America [Green Lantern [Hal Jordan] (origin); Superman [Clark Kent] (Earth-1)]";
		CharacterFieldParser parser = createParser(text);
		CharacterFieldWalker cw = walk(parser);
		assertEquals(4, cw.characters.size());

	}
}
