package org.comicwiki.gcd.parser;
// Generated from Team.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TeamParser}.
 */
public interface TeamListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TeamParser#characters}.
	 * @param ctx the parse tree
	 */
	void enterCharacters(TeamParser.CharactersContext ctx);
	/**
	 * Exit a parse tree produced by {@link TeamParser#characters}.
	 * @param ctx the parse tree
	 */
	void exitCharacters(TeamParser.CharactersContext ctx);
	/**
	 * Enter a parse tree produced by {@link TeamParser#teams}.
	 * @param ctx the parse tree
	 */
	void enterTeams(TeamParser.TeamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TeamParser#teams}.
	 * @param ctx the parse tree
	 */
	void exitTeams(TeamParser.TeamsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code noTeam}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void enterNoTeam(TeamParser.NoTeamContext ctx);
	/**
	 * Exit a parse tree produced by the {@code noTeam}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void exitNoTeam(TeamParser.NoTeamContext ctx);
	/**
	 * Enter a parse tree produced by the {@code teamAlt}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void enterTeamAlt(TeamParser.TeamAltContext ctx);
	/**
	 * Exit a parse tree produced by the {@code teamAlt}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void exitTeamAlt(TeamParser.TeamAltContext ctx);
	/**
	 * Enter a parse tree produced by the {@code teamStandard}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void enterTeamStandard(TeamParser.TeamStandardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code teamStandard}
	 * labeled alternative in {@link TeamParser#team}.
	 * @param ctx the parse tree
	 */
	void exitTeamStandard(TeamParser.TeamStandardContext ctx);
	/**
	 * Enter a parse tree produced by {@link TeamParser#aliases}.
	 * @param ctx the parse tree
	 */
	void enterAliases(TeamParser.AliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TeamParser#aliases}.
	 * @param ctx the parse tree
	 */
	void exitAliases(TeamParser.AliasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TeamParser#notes}.
	 * @param ctx the parse tree
	 */
	void enterNotes(TeamParser.NotesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TeamParser#notes}.
	 * @param ctx the parse tree
	 */
	void exitNotes(TeamParser.NotesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TeamParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(TeamParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TeamParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(TeamParser.CharacterContext ctx);
}