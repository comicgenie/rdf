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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.spark.sql.Row;
import org.comicwiki.gcd.parser.CharacterLexer;
import org.comicwiki.gcd.parser.CharacterParser;
import org.comicwiki.gcd.tables.StoryTable;

public final class CharacterFieldParser implements
		FieldParser<StoryTable.Fields.Character> {

	private CharacterWalker characterWalker;
	private boolean failOnParse;
	private FileOutputStream fos;

	public CharacterFieldParser(File resourceDirectory) {
		characterWalker = new CharacterWalker(resourceDirectory);
	}
	
	public CharacterFieldParser(File resourceDirectory, boolean failOnParse, FileOutputStream fos) {
		characterWalker = new CharacterWalker(resourceDirectory);
		this.failOnParse = failOnParse;
		this.fos = fos;
	}

	public StoryTable.Fields.Character createCharacterField(int row, String text) {
		if(text == null || text.isEmpty()) {
			return new StoryTable.Fields.Character();
		}
		
		text = CharacterFieldCleaner.cleanSemicolonInParanthesis(text);
		text = CharacterFieldCleaner.cleanCommaInBrackets(text);
		
		CharacterLexer lexer = new CharacterLexer(new ANTLRInputStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CharacterParser parser = new CharacterParser(tokens);
		if(failOnParse) {
			parser.setErrorHandler(new BailErrorStrategy());
		}	
		ParseTree tree = null;
		try {
			tree = parser.teams();
		} catch (Exception e1) {
			System.out.println(text);
			try {
				fos.write((row + ". " + text + "\r\n").getBytes());
			} catch (IOException e) {
			} 
			return new StoryTable.Fields.Character();
		}
		
		ParseTreeWalker walker = new ParseTreeWalker();
		try {
			walker.walk(characterWalker, tree);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return characterWalker.getCharacterField();
	}

	private String clean(String input) {
		return input.trim().replaceAll("--", "<DOUBLE_HYPHEN>")
				.replaceAll("V: ", "VILLAIN: ")
				.replaceAll("I: ", "INTRODUCTION: ")
				.replaceAll("(^|' ')[A-Z]:", "").replaceAll("aka[.]?[ ]* ", "")
				.replaceAll("[ ]{2,}", " ").replaceAll("\\[ ", "[")
				.replaceAll(" \\]", "]").replaceAll("\\( ", "(")
				.replaceAll(" \\)", ")").replaceAll(", ", ",")
				.replaceAll(" ,", ",");
	}

	@Override
	public StoryTable.Fields.Character parse(int field, Row row) {
		if(row.isNullAt(field)) {
			return null;
		}
		String characters = clean(row.getString(field));
		
		return createCharacterField(row.getInt(0), characters);
	}
}
