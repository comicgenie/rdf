package org.comicwiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HonorificExpander {
	private static Pattern captain = Pattern.compile(
			"^(cap'n|capt)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern doctor = Pattern.compile(
			"^(dr|doc)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern sergent = Pattern.compile(
			"^(sgt)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern lieutenant = Pattern.compile(
			"^(lt)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern professor = Pattern.compile(
			"^(prof)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern general = Pattern.compile(
			"^(gen)$", Pattern.CASE_INSENSITIVE);
	
	private static Pattern major = Pattern.compile(
			"^(maj)$", Pattern.CASE_INSENSITIVE);	
	
	public static String expand(String text) {
		Matcher matcher = captain.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Captain");
		}
		
		matcher = doctor.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Doctor");
		}
		
		matcher = sergent.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Sergeant");
		}
		
		matcher = lieutenant.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Lieutenant");
		}
		
		matcher = professor.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Professor");
		}
		
		matcher = general.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("General");
		}
		
		matcher = major.matcher(text);
		if(matcher.matches()) {
			return matcher.replaceAll("Major");
		}
		//dr|sgt|col|
		return text;
	}
}
