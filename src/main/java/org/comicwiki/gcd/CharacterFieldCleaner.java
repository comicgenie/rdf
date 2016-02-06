package org.comicwiki.gcd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterFieldCleaner {
	//("(\\(.*;+.*\\))"
	//"(\\[.*,+.*\\])"

	private static Pattern semicolon = Pattern.compile("\\(.*?\\)",
			Pattern.CASE_INSENSITIVE);
	
	private static Pattern comma = Pattern.compile("\\[.*?\\]",
			Pattern.CASE_INSENSITIVE);
	
	private static Pattern alsoAs = Pattern.compile("\\([ ]*(also as |as |also appears as )",
			Pattern.CASE_INSENSITIVE);

	public static String alsoAs(String text) {
		Matcher matcher = alsoAs.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (matcher.find()) {
			matcher.appendReplacement(sb, "(");
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
