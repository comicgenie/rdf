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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.comicwiki.IRI;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.CharacterBaseListener;
import org.comicwiki.gcd.parser.CharacterParser.CharacterContext;
import org.comicwiki.gcd.parser.CharacterParser.CharactersContext;
import org.comicwiki.gcd.parser.CharacterParser.TeamAltContext;
import org.comicwiki.gcd.parser.CharacterParser.TeamStandardContext;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTable.Fields;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.OrganizationType;

public final class CharacterWalker extends CharacterBaseListener {

	public ArrayList<String> errors = new ArrayList<>();

	public HashMap<String, ComicCharacter> characters = new HashMap<>(5);

	public HashMap<String, ComicOrganization> organizations = new HashMap<>(2);
	
	private ComicOrganization currentOrganization;

	private boolean hasOrganization = false;

	private List<String> marvelOrganizationList;

	private ThingFactory thingFactory;

	private CharacterCreator characterCreator;

	public CharacterWalker(ThingFactory thingFactory, CharacterCreator characterCreator, File resourceDir) {
		this.thingFactory = thingFactory;
		this.characterCreator = characterCreator;
		try {
			marvelOrganizationList = Files
					.readAllLines(
							new File(resourceDir, 
									"MarvelOrganizations.txt")
									.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StoryTable.Fields.Character getCharacterField() {
		Fields.Character characterField = new StoryTable.Fields.Character();
		characterField.comicCharacters = characters.values();
		characterField.comicOrganizations = organizations.values();
		return characterField;
	}

	@Override
	public void enterTeamAlt(TeamAltContext ctx) {
		super.enterTeamAlt(ctx);
		startOrganization(ctx.TEAM().getText());
	}

	@Override
	public void exitTeamAlt(TeamAltContext ctx) {
		super.exitTeamAlt(ctx);
		stopOrganization();
	}

	@Override
	public void enterTeamStandard(TeamStandardContext ctx) {
		super.enterTeamStandard(ctx);
		startOrganization(ctx.WORD().getText());
	}

	@Override
	public void exitTeamStandard(TeamStandardContext ctx) {
		super.exitTeamStandard(ctx);
		stopOrganization();
	}

	private void stopOrganization() {
		hasOrganization = false;
	}

	private void startOrganization(String organizationName) {
		if (organizationName != null && !organizationName.isEmpty()) {
			currentOrganization = thingFactory.create(ComicOrganization.class);
			currentOrganization.name = organizationName.trim();
		}
	}

	private boolean isOrganization(String name) {
		String text = name.trim();
		String noDots = text.replace("[.]", "");
		return marvelOrganizationList.contains(text)
				|| marvelOrganizationList.contains("The " + text)
				|| marvelOrganizationList.contains(noDots)
				|| marvelOrganizationList.contains("The " + noDots);
	}

	private void addCharactersFromOrganization(CharacterContext characterContext) {
		if(characterContext.aliases() != null) {
			if(characterContext.aliases().WORD() != null) {
				for (TerminalNode ac : characterContext.aliases().WORD()) {
					ComicCharacter cc = thingFactory.create(ComicCharacter.class);
					cc.name = ac.getText().trim();
					addCharacter(cc);
				}			
			}			
		}
	}

	private OrganizationType getOrganizationType(String text) {
		String lc = text.trim().toLowerCase();
		for (OrganizationType orgType : OrganizationType.values()) {
			if (lc.contains(orgType.name())) {
				return orgType;
			}
		}
		return null;
	}

	@Override
	public void enterCharacters(CharactersContext ctx) {
		super.enterCharacters(ctx);
		for (CharacterContext characterContext : ctx.character()) {
			String organizationOrCharacterText = characterContext.WORD()
					.getText();
			OrganizationType organizationType = getOrganizationType(organizationOrCharacterText);
			if (organizationType != null
					|| isOrganization(organizationOrCharacterText)) {
				addCharactersFromOrganization(characterContext);
			} else {
				ComicCharacter character = characterCreator
						.createCharacter(characterContext);
				if (hasOrganization) {
					addOrganizationMember(IRI.create(character.instanceId));
				}
				addCharacter(character);
			}
		}
	}

	private void addCharacter(ComicCharacter newCharacter) {
		characters.put(newCharacter.name, newCharacter);
	}

	private void addOrganizationMember(IRI comicCharacter) {
		currentOrganization.members.add(comicCharacter);
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		super.visitErrorNode(node);
		errors.add(node.getText().trim());
	}
}