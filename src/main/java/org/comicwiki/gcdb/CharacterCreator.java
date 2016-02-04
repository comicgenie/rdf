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

import org.antlr.v4.runtime.tree.TerminalNode;
import org.comicwiki.gcdb.parser.CharacterParser.CharacterContext;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicUniverse;

public class CharacterCreator {

	public static void merge(ComicCharacter newCharacter,
			ComicCharacter oldCharacter) {
		/*
		for (String identity : newCharacter.getIdentities()) {
			oldCharacter.addIdentity(identity);
		}

		for (String note : newCharacter.getNotes()) {
			oldCharacter.addNote(note);
		}
		*/
	}

	public static ComicCharacter createCharacter(CharacterContext ctx) {

		ComicCharacter character = new ComicCharacter();

		String name = ctx.WORD().getText().trim().replace("also as", "")
				.replace("^as ", "");
		character.name = name;

		String universe = null;
		for (TerminalNode ac : ctx.notes().WORD()) {
			String note = ac.getText().trim();
			if (!note.isEmpty() && !note.matches("<.*>")
					&& !exclude(note.toLowerCase())) {
				if (note.startsWith("Earth")) {
					universe = note;
					character.universe = new ComicUniverse(note);
				} else {
					character.creativeWork.comment.add(note);
				}				
			}
		}

		for (TerminalNode ac : ctx.aliases().WORD()) {
			String identity = ac.getText().trim();
			identity = identity.replace("also as", "")
					.replace("also appears as ", "").replace("^as ", "").trim();
			if (universe != null) {
				identity = identity + ":" + universe;
			}
		//	character.addIdentity(identity);
		}
		return character;
	}

	private static boolean exclude(String value) {
		for (String s : excludes) {
			if (s.equals(value)) {
				return true;
			}
		}
		return false;
	}

	private static final String[] excludes = { "death", "dies", "guest-star",
			"guest", "cameo", "cameos", "cameo flashback", "first appearance",
			" 1st appearance", "only appearance", "first", "1st", "joins",
			"join", "inset", "origin", "intro", "introduction", "photo",
			"flashback", "mentioned", "host", "narrator" };
}
