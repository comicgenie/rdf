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
package org.comicwiki.gcdb.parser;
// Generated from Character.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CharacterParser}.
 */
public interface CharacterListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CharacterParser#characters}.
	 * @param ctx the parse tree
	 */
	void enterCharacters(CharacterParser.CharactersContext ctx);
	/**
	 * Exit a parse tree produced by {@link CharacterParser#characters}.
	 * @param ctx the parse tree
	 */
	void exitCharacters(CharacterParser.CharactersContext ctx);
	/**
	 * Enter a parse tree produced by {@link CharacterParser#teams}.
	 * @param ctx the parse tree
	 */
	void enterTeams(CharacterParser.TeamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CharacterParser#teams}.
	 * @param ctx the parse tree
	 */
	void exitTeams(CharacterParser.TeamsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code noTeam}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void enterNoTeam(CharacterParser.NoTeamContext ctx);
	/**
	 * Exit a parse tree produced by the {@code noTeam}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void exitNoTeam(CharacterParser.NoTeamContext ctx);
	/**
	 * Enter a parse tree produced by the {@code teamAlt}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void enterTeamAlt(CharacterParser.TeamAltContext ctx);
	/**
	 * Exit a parse tree produced by the {@code teamAlt}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void exitTeamAlt(CharacterParser.TeamAltContext ctx);
	/**
	 * Enter a parse tree produced by the {@code teamStandard}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void enterTeamStandard(CharacterParser.TeamStandardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code teamStandard}
	 * labeled alternative in {@link CharacterParser#team}.
	 * @param ctx the parse tree
	 */
	void exitTeamStandard(CharacterParser.TeamStandardContext ctx);
	/**
	 * Enter a parse tree produced by {@link CharacterParser#aliases}.
	 * @param ctx the parse tree
	 */
	void enterAliases(CharacterParser.AliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link CharacterParser#aliases}.
	 * @param ctx the parse tree
	 */
	void exitAliases(CharacterParser.AliasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link CharacterParser#notes}.
	 * @param ctx the parse tree
	 */
	void enterNotes(CharacterParser.NotesContext ctx);
	/**
	 * Exit a parse tree produced by {@link CharacterParser#notes}.
	 * @param ctx the parse tree
	 */
	void exitNotes(CharacterParser.NotesContext ctx);
	/**
	 * Enter a parse tree produced by {@link CharacterParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(CharacterParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CharacterParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(CharacterParser.CharacterContext ctx);
}