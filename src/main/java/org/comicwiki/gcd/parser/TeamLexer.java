package org.comicwiki.gcd.parser;
// Generated from Team.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TeamLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, SEMICOLON=6, COMMA=7, WORD=8, 
		TEAM=9, NOTE_TYPE=10, WS=11, EM_DASH=12;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "SEMICOLON", "COMMA", "WORD", 
		"TEAM", "NOTE_TYPE", "WS", "BLANK", "EM_DASH"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'['", "']'", "'('", "')'", "' '", "';'", "','", null, null, null, 
		null, "'<EM_DASH>'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "SEMICOLON", "COMMA", "WORD", "TEAM", 
		"NOTE_TYPE", "WS", "EM_DASH"
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


	public TeamLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Team.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\16\u00ac\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\t\6\t-\n\t\r\t\16\t.\3\n\3\n\3\n\3\13\7\13\65\n"+
		"\13\f\13\16\138\13\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13"+
		"C\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13M\n\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\5\13W\n\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13k\n\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0094\n\13"+
		"\3\f\6\f\u0097\n\f\r\f\16\f\u0098\3\f\3\f\3\r\7\r\u009e\n\r\f\r\16\r\u00a1"+
		"\13\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\2\2\17\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\2\33\16\3\2\6\7\2*+..="+
		"=]]__\3\2\"\"\3\2UU\5\2\13\f\17\17\"\"\u00b8\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2"+
		"\5\37\3\2\2\2\7!\3\2\2\2\t#\3\2\2\2\13%\3\2\2\2\r\'\3\2\2\2\17)\3\2\2"+
		"\2\21,\3\2\2\2\23\60\3\2\2\2\25\66\3\2\2\2\27\u0096\3\2\2\2\31\u009f\3"+
		"\2\2\2\33\u00a2\3\2\2\2\35\36\7]\2\2\36\4\3\2\2\2\37 \7_\2\2 \6\3\2\2"+
		"\2!\"\7*\2\2\"\b\3\2\2\2#$\7+\2\2$\n\3\2\2\2%&\7\"\2\2&\f\3\2\2\2\'(\7"+
		"=\2\2(\16\3\2\2\2)*\7.\2\2*\20\3\2\2\2+-\n\2\2\2,+\3\2\2\2-.\3\2\2\2."+
		",\3\2\2\2./\3\2\2\2/\22\3\2\2\2\60\61\5\21\t\2\61\62\5\33\16\2\62\24\3"+
		"\2\2\2\63\65\t\3\2\2\64\63\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2"+
		"\2\2\67\u0093\3\2\2\28\66\3\2\2\29:\7X\2\2:;\7K\2\2;<\7N\2\2<=\7N\2\2"+
		"=>\7C\2\2>?\7K\2\2?@\7P\2\2@B\3\2\2\2AC\t\4\2\2BA\3\2\2\2BC\3\2\2\2CD"+
		"\3\2\2\2D\u0094\7<\2\2EF\7I\2\2FG\7W\2\2GH\7G\2\2HI\7U\2\2IJ\7V\2\2JL"+
		"\3\2\2\2KM\t\4\2\2LK\3\2\2\2LM\3\2\2\2MN\3\2\2\2N\u0094\7<\2\2OP\7E\2"+
		"\2PQ\7C\2\2QR\7O\2\2RS\7G\2\2ST\7Q\2\2TV\3\2\2\2UW\t\4\2\2VU\3\2\2\2V"+
		"W\3\2\2\2WX\3\2\2\2X\u0094\7<\2\2YZ\7E\2\2Z[\7C\2\2[\\\7O\2\2\\]\7G\2"+
		"\2]^\7Q\2\2^_\7\"\2\2_`\7H\2\2`a\7N\2\2ab\7C\2\2bc\7U\2\2cd\7J\2\2de\7"+
		"D\2\2ef\7C\2\2fg\7E\2\2gh\7M\2\2hj\3\2\2\2ik\t\4\2\2ji\3\2\2\2jk\3\2\2"+
		"\2kl\3\2\2\2l\u0094\7<\2\2mn\7K\2\2no\7P\2\2op\7V\2\2pq\7T\2\2qr\7Q\2"+
		"\2rs\7F\2\2st\7W\2\2tu\7E\2\2uv\7V\2\2vw\7K\2\2wx\7Q\2\2xy\7P\2\2y\u0094"+
		"\7<\2\2z{\7H\2\2{|\7G\2\2|}\7C\2\2}~\7V\2\2~\177\7W\2\2\177\u0080\7T\2"+
		"\2\u0080\u0081\7G\2\2\u0081\u0094\7<\2\2\u0082\u0083\7I\2\2\u0083\u0084"+
		"\7W\2\2\u0084\u0085\7G\2\2\u0085\u0086\7U\2\2\u0086\u0087\7V\2\2\u0087"+
		"\u0088\7\"\2\2\u0088\u0089\7C\2\2\u0089\u008a\7R\2\2\u008a\u008b\7R\2"+
		"\2\u008b\u008c\7G\2\2\u008c\u008d\7C\2\2\u008d\u008e\7T\2\2\u008e\u008f"+
		"\7C\2\2\u008f\u0090\7P\2\2\u0090\u0091\7E\2\2\u0091\u0092\7G\2\2\u0092"+
		"\u0094\7<\2\2\u00939\3\2\2\2\u0093E\3\2\2\2\u0093O\3\2\2\2\u0093Y\3\2"+
		"\2\2\u0093m\3\2\2\2\u0093z\3\2\2\2\u0093\u0082\3\2\2\2\u0094\26\3\2\2"+
		"\2\u0095\u0097\t\5\2\2\u0096\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0096"+
		"\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\b\f\2\2\u009b"+
		"\30\3\2\2\2\u009c\u009e\t\3\2\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2"+
		"\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\32\3\2\2\2\u00a1\u009f"+
		"\3\2\2\2\u00a2\u00a3\7>\2\2\u00a3\u00a4\7G\2\2\u00a4\u00a5\7O\2\2\u00a5"+
		"\u00a6\7a\2\2\u00a6\u00a7\7F\2\2\u00a7\u00a8\7C\2\2\u00a8\u00a9\7U\2\2"+
		"\u00a9\u00aa\7J\2\2\u00aa\u00ab\7@\2\2\u00ab\34\3\2\2\2\f\2.\66BLVj\u0093"+
		"\u0098\u009f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}