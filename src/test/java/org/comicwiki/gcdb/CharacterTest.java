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
package org.comicwiki.gcdb;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.test.runtime.java.BaseTest;
import org.comicwiki.gcdb.parser.CharacterLexer;
import org.comicwiki.gcdb.parser.CharacterParser;
import org.junit.Test;

public class CharacterTest extends BaseTest {

	@Test
	public void testB() throws IOException {
		String text = "Batman; Robin; The Thing";
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		ParseTree tree = parser.characters();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(), tree);
	}

	//
	@Test
	public void testC() throws IOException {
		String text = "Batman; Robin*(batman's sidekick); The Thing [Ben; MyThing](a member of F4, badass)";
		String fields = "ABC (,hello world, hello universe)";
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(fields));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(), tree);
	}

	@Test
	public void testE() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";

		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text2));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(), tree);
	}

	@Test
	public void testF() throws IOException {
		String text = "Agent Daisy Caruthers [Dugan];VILLAIN: Sentinels; Zigfried Trask;CAMEOS: The Consortium";
		String text2 = "CAMEO FLASHBACKS: Dietrich Trask; Wolverine; Cyclops; Iceman; Angel; Storm; Gambit";
		String text3 = "X-Men<DOUBLE_HYPHEN>Jean Grey; Rogue";

		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text3));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(), tree);
	}

	@Test
	public void testTeam() throws IOException {
		String text = "Justice League of America [Green Lantern [Hal Jordan] (origin); Superman [Clark Kent] (Earth-1)]";
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);

		ParseTree tree = parser.team();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new CharacterWalker(), tree);
	}
}
