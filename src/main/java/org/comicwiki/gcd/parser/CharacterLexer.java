package org.comicwiki.gcd.parser;
// Generated from Character.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CharacterLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, SEMICOLON=7, COMMA=8, 
		WORD=9, TEAM=10, NOTE_TYPE=11, WS=12, EM_DASH=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "SEMICOLON", "COMMA", 
		"WORD", "TEAM", "NOTE_TYPE", "WS", "BLANK", "EM_DASH"
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


	public CharacterLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Character.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17\u00b4\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\6\n\65\n\n"+
		"\r\n\16\n\66\3\13\3\13\3\13\3\f\7\f=\n\f\f\f\16\f@\13\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\5\fK\n\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\fU\n"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f_\n\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\fs\n\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\5\f\u009c\n\f\3\r\6\r\u009f\n\r\r\r\16\r\u00a0\3\r\3\r\3\16\7\16\u00a6"+
		"\n\16\f\16\16\16\u00a9\13\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\2\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\2\35\17\3\2\6\7\2*+..==]]__\3\2\"\"\3\2UU\5\2\13\f\17\17\"\"\u00c0"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5%\3\2\2\2\7\'\3\2\2\2\t)\3\2"+
		"\2\2\13+\3\2\2\2\r-\3\2\2\2\17/\3\2\2\2\21\61\3\2\2\2\23\64\3\2\2\2\25"+
		"8\3\2\2\2\27>\3\2\2\2\31\u009e\3\2\2\2\33\u00a7\3\2\2\2\35\u00aa\3\2\2"+
		"\2\37 \7\"\2\2 !\7c\2\2!\"\7p\2\2\"#\7f\2\2#$\7\"\2\2$\4\3\2\2\2%&\7]"+
		"\2\2&\6\3\2\2\2\'(\7_\2\2(\b\3\2\2\2)*\7*\2\2*\n\3\2\2\2+,\7+\2\2,\f\3"+
		"\2\2\2-.\7\"\2\2.\16\3\2\2\2/\60\7=\2\2\60\20\3\2\2\2\61\62\7.\2\2\62"+
		"\22\3\2\2\2\63\65\n\2\2\2\64\63\3\2\2\2\65\66\3\2\2\2\66\64\3\2\2\2\66"+
		"\67\3\2\2\2\67\24\3\2\2\289\5\23\n\29:\5\35\17\2:\26\3\2\2\2;=\t\3\2\2"+
		"<;\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?\u009b\3\2\2\2@>\3\2\2\2AB\7"+
		"X\2\2BC\7K\2\2CD\7N\2\2DE\7N\2\2EF\7C\2\2FG\7K\2\2GH\7P\2\2HJ\3\2\2\2"+
		"IK\t\4\2\2JI\3\2\2\2JK\3\2\2\2KL\3\2\2\2L\u009c\7<\2\2MN\7I\2\2NO\7W\2"+
		"\2OP\7G\2\2PQ\7U\2\2QR\7V\2\2RT\3\2\2\2SU\t\4\2\2TS\3\2\2\2TU\3\2\2\2"+
		"UV\3\2\2\2V\u009c\7<\2\2WX\7E\2\2XY\7C\2\2YZ\7O\2\2Z[\7G\2\2[\\\7Q\2\2"+
		"\\^\3\2\2\2]_\t\4\2\2^]\3\2\2\2^_\3\2\2\2_`\3\2\2\2`\u009c\7<\2\2ab\7"+
		"E\2\2bc\7C\2\2cd\7O\2\2de\7G\2\2ef\7Q\2\2fg\7\"\2\2gh\7H\2\2hi\7N\2\2"+
		"ij\7C\2\2jk\7U\2\2kl\7J\2\2lm\7D\2\2mn\7C\2\2no\7E\2\2op\7M\2\2pr\3\2"+
		"\2\2qs\t\4\2\2rq\3\2\2\2rs\3\2\2\2st\3\2\2\2t\u009c\7<\2\2uv\7K\2\2vw"+
		"\7P\2\2wx\7V\2\2xy\7T\2\2yz\7Q\2\2z{\7F\2\2{|\7W\2\2|}\7E\2\2}~\7V\2\2"+
		"~\177\7K\2\2\177\u0080\7Q\2\2\u0080\u0081\7P\2\2\u0081\u009c\7<\2\2\u0082"+
		"\u0083\7H\2\2\u0083\u0084\7G\2\2\u0084\u0085\7C\2\2\u0085\u0086\7V\2\2"+
		"\u0086\u0087\7W\2\2\u0087\u0088\7T\2\2\u0088\u0089\7G\2\2\u0089\u009c"+
		"\7<\2\2\u008a\u008b\7I\2\2\u008b\u008c\7W\2\2\u008c\u008d\7G\2\2\u008d"+
		"\u008e\7U\2\2\u008e\u008f\7V\2\2\u008f\u0090\7\"\2\2\u0090\u0091\7C\2"+
		"\2\u0091\u0092\7R\2\2\u0092\u0093\7R\2\2\u0093\u0094\7G\2\2\u0094\u0095"+
		"\7C\2\2\u0095\u0096\7T\2\2\u0096\u0097\7C\2\2\u0097\u0098\7P\2\2\u0098"+
		"\u0099\7E\2\2\u0099\u009a\7G\2\2\u009a\u009c\7<\2\2\u009bA\3\2\2\2\u009b"+
		"M\3\2\2\2\u009bW\3\2\2\2\u009ba\3\2\2\2\u009bu\3\2\2\2\u009b\u0082\3\2"+
		"\2\2\u009b\u008a\3\2\2\2\u009c\30\3\2\2\2\u009d\u009f\t\5\2\2\u009e\u009d"+
		"\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1"+
		"\u00a2\3\2\2\2\u00a2\u00a3\b\r\2\2\u00a3\32\3\2\2\2\u00a4\u00a6\t\3\2"+
		"\2\u00a5\u00a4\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8"+
		"\3\2\2\2\u00a8\34\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ab\7>\2\2\u00ab"+
		"\u00ac\7G\2\2\u00ac\u00ad\7O\2\2\u00ad\u00ae\7a\2\2\u00ae\u00af\7F\2\2"+
		"\u00af\u00b0\7C\2\2\u00b0\u00b1\7U\2\2\u00b1\u00b2\7J\2\2\u00b2\u00b3"+
		"\7@\2\2\u00b3\36\3\2\2\2\f\2\66>JT^r\u009b\u00a0\u00a7\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}