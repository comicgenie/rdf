package org.comicwiki.gcd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterFieldCleaner {

	private static Pattern semicolon = Pattern.compile("\\(.*?\\)",
			Pattern.CASE_INSENSITIVE);

	private static Pattern colonParen = Pattern.compile("\\(.*?\\)",
			Pattern.CASE_INSENSITIVE);

	private static Pattern colonBracket = Pattern.compile("\\[.*?\\]",
			Pattern.CASE_INSENSITIVE);

	private static Pattern comma = Pattern.compile("\\[.*?\\]",
			Pattern.CASE_INSENSITIVE);

	// private static Pattern alsoAs =
	// Pattern.compile("\\[.*(?=(also as |as |also appears as )).*\\]",
	// Pattern.CASE_INSENSITIVE);
	private static Pattern alsoAs = Pattern.compile(
			"[ ]*(also as |as |also appears as )", Pattern.CASE_INSENSITIVE);

	// .replaceAll("aka[.]?[ ]* ", "");
	public static String organizationMarker(String text) {
		return text.trim().replaceAll("--", "<EM_DASH>");
	}

	public static String cleanAll(String text) {
		 text = cleanCommaInBrackets(text);
		 text = cleanSemicolonInParanthesis(text);
		 text = cleanColon(text);
		 text = cleanColonBracket(text);
		text = repair(text);
		text = alsoAs(text);
		text = organizationMarker(text);
		return text;
	}

	
	public static String repair(String input) {
		char[] tokens = input.toCharArray();
		boolean LEFT_P = false, LEFT_B = false;
		for (int i = 0; i < tokens.length; i++) {
			char t = tokens[i];
			if (t == '(') {
				LEFT_P = true;
				LEFT_B = false;
			} else if (t == '[') {
				LEFT_B = true;
				LEFT_P = false;
			} else if (t == ']') {
				LEFT_B = false;
				if (LEFT_P) {
					tokens[i] = ')';			
					LEFT_P = false;
				} 
			} else if (t == ')') { 
				LEFT_P = false;
				if (LEFT_B) {
					tokens[i] = ']';
					LEFT_B = false;
				}
			}
		}
		if(LEFT_B) {
			return new String(tokens) +"]";
		} else if(LEFT_P) {
			return new String(tokens) + ")";
		}
		return new String(tokens);
	}
	
	public static String alsoAs(String text) {
		Matcher matcher = alsoAs.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String cleanSemicolonInParanthesis(String text) {
		Matcher matcher = semicolon.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			String t = matcher.group().replaceAll(";", ",");
			matcher.appendReplacement(sb, Matcher.quoteReplacement(t));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String cleanColonBracket(String text) {
		Matcher matcher = colonBracket.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			String t = matcher.group().replaceAll(":", ";");
			matcher.appendReplacement(sb, Matcher.quoteReplacement(t));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String cleanColon(String text) {
		Matcher matcher = colonParen.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			String t = matcher.group().replaceAll(":", ",");
			matcher.appendReplacement(sb, Matcher.quoteReplacement(t));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String cleanCommaInBrackets(String text) {
		Matcher matcher = comma.matcher(text);

		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			String t = matcher.group().replaceAll(",", ";");
			matcher.appendReplacement(sb, Matcher.quoteReplacement(t));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
