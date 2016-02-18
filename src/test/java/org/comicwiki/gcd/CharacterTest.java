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

import static org.junit.Assert.*;

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
import org.comicwiki.gcd.CharacterFieldCleaner;
import org.comicwiki.gcd.CharacterWalker;
import org.comicwiki.gcd.parser.CharacterLexer;
import org.comicwiki.gcd.parser.CharacterParser;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CharacterTest extends BaseTest {

	private File resourceDir = new File("./src/main/resources/comics");

	private CharacterParser createParser(String text) {
		text = CharacterFieldCleaner.cleanSemicolonInParanthesis(text);
		System.out.println(text);
		text = CharacterFieldCleaner.cleanCommaInBrackets(text);
		System.out.println(text);
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());
		return parser;
	}

	private CharacterWalker walk(CharacterParser parser) {
		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTreeWalker walker = new ParseTreeWalker();
		CharacterWalker cWalker = new CharacterWalker(thingFactory,
				new IRICache(), characterCreator, resourceDir);
		walker.walk(cWalker, parser.characters());
		return cWalker;
	}

	/**
	 * This is not according to spec. Only Parenthesis
	 */
	/*
	 * @Test public void testOnlyParenthesis() throws IOException { String text
	 * = "(woman on ice skates)"; CharacterParser parser = createParser(text);
	 * walk(parser); }
	 */
	@Test
	public void testMismatch() throws IOException {
		String text = "Batman [Bruce Wayne]; Robin [Dick Grayson]; John Barham (mention only;death); James Barham (millionaire;death); Adam Barham (Jame's cousin); John Gorley; Sheriff Martin; Robert Cray (villain;introduction); Vance Sonderson [as Jay Sonderson] (villain,gun smuggler,introduction)";
		CharacterParser parser = createParser(text);
		CharacterWalker cw = walk(parser);
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
		CharacterParser parser = createParser(text);
		walk(parser);
	}

	@Test
	public void testB() throws IOException {
		String text = "Batman; Robin; The Thing";
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(thingFactory, new IRICache(),
				characterCreator, resourceDir), parser.characters());
	}

	//
	@Test
	public void testC() throws IOException {
		String text = "Batman; Robin*(batman's sidekick); The Thing [Ben; MyThing](a member of F4, badass)";
		String fields = "ABC (,hello world, hello universe)";
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(fields));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(thingFactory, new IRICache(),
				characterCreator, resourceDir), tree);
	}

	@Test
	public void testE() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";

		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text2));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(thingFactory, new IRICache(),
				characterCreator, resourceDir), tree);
	}

	@Test
	public void testF() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";
		String text3 = "X-Men<DOUBLE_HYPHEN>Jean Grey; Rogue";

		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text3));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());

		ThingCache thingCache = new ThingCache(new Repositories(),
				new IRICache(), new ResourceIDCache());
		ThingFactory thingFactory = new ThingFactory(thingCache);
		CharacterCreator characterCreator = new CharacterCreator(thingFactory);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(thingFactory, new IRICache(),
				characterCreator, resourceDir), tree);
	}

	@Test
	public void testTeam() throws IOException {
		String text = "Justice League of America [Green Lantern [Hal Jordan] (origin); Superman [Clark Kent] (Earth-1)]";
		CharacterParser parser = createParser(text);
		CharacterWalker cw = walk(parser);
		assertEquals(4, cw.characters.size());

	}
}
