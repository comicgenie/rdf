package org.comicwiki.gcd.parser;
// Generated from Character.g4 by ANTLR 4.5.1
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CharacterParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, SEMICOLON=7, COMMA=8, 
		WORD=9, TEAM=10, NOTE_TYPE=11, WS=12, EM_DASH=13;
	public static final int
		RULE_characters = 0, RULE_teams = 1, RULE_team = 2, RULE_aliases = 3, 
		RULE_notes = 4, RULE_character = 5;
	public static final String[] ruleNames = {
		"characters", "teams", "team", "aliases", "notes", "character"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "' and '", "'['", "']'", "'('", "')'", "' '", "';'", "','", null, 
		null, null, null, "'<EM_DASH>'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "SEMICOLON", "COMMA", "WORD", 
		"TEAM", "NOTE_TYPE", "WS", "EM_DASH"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Character.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CharacterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CharactersContext extends ParserRuleContext {
		public List<CharacterContext> character() {
			return getRuleContexts(CharacterContext.class);
		}
		public CharacterContext character(int i) {
			return getRuleContext(CharacterContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(CharacterParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(CharacterParser.SEMICOLON, i);
		}
		public CharactersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_characters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterCharacters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitCharacters(this);
		}
	}

	public final CharactersContext characters() throws RecognitionException {
		CharactersContext _localctx = new CharactersContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_characters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			character();
			setState(19);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(14);
					_la = _input.LA(1);
					if (_la==T__0 || _la==SEMICOLON) {
						{
						setState(13);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==SEMICOLON) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
					}

					setState(16);
					character();
					}
					} 
				}
				setState(21);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(23);
			_la = _input.LA(1);
			if (_la==T__0 || _la==SEMICOLON) {
				{
				setState(22);
				_la = _input.LA(1);
				if ( !(_la==T__0 || _la==SEMICOLON) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TeamsContext extends ParserRuleContext {
		public List<TeamContext> team() {
			return getRuleContexts(TeamContext.class);
		}
		public TeamContext team(int i) {
			return getRuleContext(TeamContext.class,i);
		}
		public TeamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_teams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterTeams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitTeams(this);
		}
	}

	public final TeamsContext teams() throws RecognitionException {
		TeamsContext _localctx = new TeamsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_teams);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(25);
				team();
				}
				}
				setState(28); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WORD) | (1L << TEAM) | (1L << NOTE_TYPE))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TeamContext extends ParserRuleContext {
		public TeamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_team; }
	 
		public TeamContext() { }
		public void copyFrom(TeamContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TeamStandardContext extends TeamContext {
		public TerminalNode WORD() { return getToken(CharacterParser.WORD, 0); }
		public CharactersContext characters() {
			return getRuleContext(CharactersContext.class,0);
		}
		public TeamStandardContext(TeamContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterTeamStandard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitTeamStandard(this);
		}
	}
	public static class NoTeamContext extends TeamContext {
		public CharactersContext characters() {
			return getRuleContext(CharactersContext.class,0);
		}
		public NoTeamContext(TeamContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterNoTeam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitNoTeam(this);
		}
	}
	public static class TeamAltContext extends TeamContext {
		public TerminalNode TEAM() { return getToken(CharacterParser.TEAM, 0); }
		public CharactersContext characters() {
			return getRuleContext(CharactersContext.class,0);
		}
		public TeamAltContext(TeamContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterTeamAlt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitTeamAlt(this);
		}
	}

	public final TeamContext team() throws RecognitionException {
		TeamContext _localctx = new TeamContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_team);
		try {
			setState(38);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new NoTeamContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				characters();
				}
				break;
			case 2:
				_localctx = new TeamAltContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(31);
				match(TEAM);
				setState(32);
				characters();
				}
				break;
			case 3:
				_localctx = new TeamStandardContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(33);
				match(WORD);
				{
				setState(34);
				match(T__1);
				setState(35);
				characters();
				setState(36);
				match(T__2);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasesContext extends ParserRuleContext {
		public List<TerminalNode> WORD() { return getTokens(CharacterParser.WORD); }
		public TerminalNode WORD(int i) {
			return getToken(CharacterParser.WORD, i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(CharacterParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(CharacterParser.SEMICOLON, i);
		}
		public AliasesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliases; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterAliases(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitAliases(this);
		}
	}

	public final AliasesContext aliases() throws RecognitionException {
		AliasesContext _localctx = new AliasesContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_aliases);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(40);
				match(T__1);
				setState(42);
				_la = _input.LA(1);
				if (_la==SEMICOLON) {
					{
					setState(41);
					match(SEMICOLON);
					}
				}

				setState(44);
				match(WORD);
				setState(49);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(45);
						match(SEMICOLON);
						setState(46);
						match(WORD);
						}
						} 
					}
					setState(51);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				}
				setState(53);
				_la = _input.LA(1);
				if (_la==SEMICOLON) {
					{
					setState(52);
					match(SEMICOLON);
					}
				}

				setState(55);
				match(T__2);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotesContext extends ParserRuleContext {
		public List<TerminalNode> WORD() { return getTokens(CharacterParser.WORD); }
		public TerminalNode WORD(int i) {
			return getToken(CharacterParser.WORD, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CharacterParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CharacterParser.COMMA, i);
		}
		public NotesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterNotes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitNotes(this);
		}
	}

	public final NotesContext notes() throws RecognitionException {
		NotesContext _localctx = new NotesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_notes);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(58);
				match(T__3);
				setState(60);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(59);
					match(COMMA);
					}
				}

				setState(62);
				match(WORD);
				setState(67);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(63);
						match(COMMA);
						setState(64);
						match(WORD);
						}
						} 
					}
					setState(69);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				setState(71);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(70);
					match(COMMA);
					}
				}

				setState(73);
				match(T__4);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CharacterContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(CharacterParser.WORD, 0); }
		public TerminalNode NOTE_TYPE() { return getToken(CharacterParser.NOTE_TYPE, 0); }
		public AliasesContext aliases() {
			return getRuleContext(AliasesContext.class,0);
		}
		public NotesContext notes() {
			return getRuleContext(NotesContext.class,0);
		}
		public CharacterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_character; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).enterCharacter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CharacterListener ) ((CharacterListener)listener).exitCharacter(this);
		}
	}

	public final CharacterContext character() throws RecognitionException {
		CharacterContext _localctx = new CharacterContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_character);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_la = _input.LA(1);
			if (_la==NOTE_TYPE) {
				{
				setState(76);
				match(NOTE_TYPE);
				}
			}

			setState(79);
			match(WORD);
			setState(98);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				{
				setState(80);
				aliases();
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(81);
					match(T__5);
					}
					}
					setState(86);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(87);
				notes();
				}
				}
				break;
			case 2:
				{
				{
				setState(89);
				notes();
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(90);
					match(T__5);
					}
					}
					setState(95);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(96);
				aliases();
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\17g\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\5\2\21\n\2\3\2\7\2\24\n\2\f"+
		"\2\16\2\27\13\2\3\2\5\2\32\n\2\3\3\6\3\35\n\3\r\3\16\3\36\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\5\4)\n\4\3\5\3\5\5\5-\n\5\3\5\3\5\3\5\7\5\62\n\5"+
		"\f\5\16\5\65\13\5\3\5\5\58\n\5\3\5\5\5;\n\5\3\6\3\6\5\6?\n\6\3\6\3\6\3"+
		"\6\7\6D\n\6\f\6\16\6G\13\6\3\6\5\6J\n\6\3\6\5\6M\n\6\3\7\5\7P\n\7\3\7"+
		"\3\7\3\7\7\7U\n\7\f\7\16\7X\13\7\3\7\3\7\3\7\3\7\7\7^\n\7\f\7\16\7a\13"+
		"\7\3\7\3\7\5\7e\n\7\3\7\2\2\b\2\4\6\b\n\f\2\3\4\2\3\3\t\tr\2\16\3\2\2"+
		"\2\4\34\3\2\2\2\6(\3\2\2\2\b:\3\2\2\2\nL\3\2\2\2\fO\3\2\2\2\16\25\5\f"+
		"\7\2\17\21\t\2\2\2\20\17\3\2\2\2\20\21\3\2\2\2\21\22\3\2\2\2\22\24\5\f"+
		"\7\2\23\20\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26\31\3\2"+
		"\2\2\27\25\3\2\2\2\30\32\t\2\2\2\31\30\3\2\2\2\31\32\3\2\2\2\32\3\3\2"+
		"\2\2\33\35\5\6\4\2\34\33\3\2\2\2\35\36\3\2\2\2\36\34\3\2\2\2\36\37\3\2"+
		"\2\2\37\5\3\2\2\2 )\5\2\2\2!\"\7\f\2\2\")\5\2\2\2#$\7\13\2\2$%\7\4\2\2"+
		"%&\5\2\2\2&\'\7\5\2\2\')\3\2\2\2( \3\2\2\2(!\3\2\2\2(#\3\2\2\2)\7\3\2"+
		"\2\2*,\7\4\2\2+-\7\t\2\2,+\3\2\2\2,-\3\2\2\2-.\3\2\2\2.\63\7\13\2\2/\60"+
		"\7\t\2\2\60\62\7\13\2\2\61/\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64"+
		"\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\668\7\t\2\2\67\66\3\2\2\2\678\3\2"+
		"\2\289\3\2\2\29;\7\5\2\2:*\3\2\2\2:;\3\2\2\2;\t\3\2\2\2<>\7\6\2\2=?\7"+
		"\n\2\2>=\3\2\2\2>?\3\2\2\2?@\3\2\2\2@E\7\13\2\2AB\7\n\2\2BD\7\13\2\2C"+
		"A\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FI\3\2\2\2GE\3\2\2\2HJ\7\n\2\2"+
		"IH\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KM\7\7\2\2L<\3\2\2\2LM\3\2\2\2M\13\3\2\2"+
		"\2NP\7\r\2\2ON\3\2\2\2OP\3\2\2\2PQ\3\2\2\2Qd\7\13\2\2RV\5\b\5\2SU\7\b"+
		"\2\2TS\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XV\3\2\2\2YZ\5\n"+
		"\6\2Ze\3\2\2\2[_\5\n\6\2\\^\7\b\2\2]\\\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3"+
		"\2\2\2`b\3\2\2\2a_\3\2\2\2bc\5\b\5\2ce\3\2\2\2dR\3\2\2\2d[\3\2\2\2e\r"+
		"\3\2\2\2\23\20\25\31\36(,\63\67:>EILOV_d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}