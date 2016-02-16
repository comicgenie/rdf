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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CharacterParser.CharacterContext;
import org.comicwiki.model.ComicCharacter;

import com.google.inject.Inject;

public class CharacterCreator {

	private static FileOutputStream fos;
	
	private ThingFactory thingFactory;

	@Inject
	public CharacterCreator(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
		
	}
	public ComicCharacter createCharacter(CharacterContext ctx) {

		if(fos == null) {
			try {
				fos = new FileOutputStream("characters.all.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		ComicCharacter character = thingFactory.create(ComicCharacter.class);

		String printName = ctx.WORD().getText();
		
		try {
			fos.write((printName + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String name = ctx.WORD().getText().trim().replace("also as", "")
				.replace("^as ", "");
		character.name = name;

		String universe = null;
		if(ctx.notes() != null) {
			for (TerminalNode ac : ctx.notes().WORD()) {
				String note = ac.getText().trim();
				try {
					fos.write(("NOTE: " + note + "\r\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!note.isEmpty() && !note.matches("<.*>")
						&& !exclude(note.toLowerCase())) {
					if (note.startsWith("Earth")) {
						universe = note;
						//character.universe = new ComicUniverse(note);
						//character.universe = ThingFactory.create(ComicUniverse.class);
						//character.universe.name = note;
					} else {
						character.creativeWork.comment.add(note);
					}				
				}
			}			
		}

		if(ctx.aliases() != null) {
			for (TerminalNode ac : ctx.aliases().WORD()) {
				String identity = ac.getText().trim();
				try {
					fos.write(("ALIAS: " + identity + "\r\n").getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				identity = identity.replace("also as", "")
						.replace("also appears as ", "").replace("^as ", "").trim();
				if (universe != null) {
					identity = identity + ":" + universe;
				}
				//TODO: 
			//	character.addIdentity(identity);
			}		
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

	//TODO: Convert to Regex
	private static final String[] excludes = { "death", "dies", "guest-star",
			"guest", "cameo", "cameos", "cameo flashback", "first appearance",
			" 1st appearance", "only appearance", "first", "1st", "joins",
			"join", "inset", "origin", "intro", "introduction", "photo",
			"flashback", "mentioned", "host", "narrator" };
}
