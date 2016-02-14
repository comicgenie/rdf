package org.comicwiki.transforms;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.comicwiki.HonorificExpander;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.RepositoryTransform;
import org.comicwiki.model.ComicCharacter;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

public final class ComicCharacterTransform implements RepositoryTransform {
	private static Pattern impliedFemale = Pattern.compile(".*(girl|woman).*",
			Pattern.CASE_INSENSITIVE);

	private static Pattern impliedMale = Pattern.compile(".*\\b(man)\\b",
			Pattern.CASE_INSENSITIVE);

	private static Pattern middleInitial = Pattern.compile("\\w{1}(\\.){1}",
			Pattern.CASE_INSENSITIVE);
	private static Pattern prefixFemale = Pattern
			.compile(
					"^(mrs|miss|ms|lady|princess|queen|dame|madam|ma'am|duchess|viscountess|countess|baroness|aunt)( |\\.){1}(.*)",
					Pattern.CASE_INSENSITIVE);

	private static Pattern prefixMale = Pattern
			.compile(
					"^(mr|sir|lord|master|king|prince|count|duke|baron|viscount|knight|earl|emperor|uncle|shiek)( |\\.){1}(.*)",
					Pattern.CASE_INSENSITIVE);
	private static Pattern prefixNeutral = Pattern
			.compile(
					"(^(ambassador|private|constable|senator|corporal|mayor|cap'n|professor|president|inspector|detective|doctor|doc|dr|sgt|col|captain|major|maj|officer|general|gen|prof|capt|sergeant|sheriff|lieutenant|lt)( |\\.){1})(.*)",
					Pattern.CASE_INSENSITIVE);

	private static Pattern suffixMale = Pattern.compile(
			".*(esq|esquire|caesar)\\.*$", Pattern.CASE_INSENSITIVE);

	private static boolean firstAndLastName(String[] tokens) {
		return tokens.length == 2;
	}

	private static boolean oneName(String[] tokens) {
		return tokens.length == 1;
	}

	private static String[] tokenize(String text) {
		return Iterables.toArray(Splitter.on(' ').trimResults()
				.omitEmptyStrings().split(text), String.class);
	}

	private final PersonNameMatcher personMatcher;

	public ComicCharacterTransform(PersonNameMatcher personMatcher) {
		this.personMatcher = personMatcher;
	}

	protected String removePrefixAndSuffix(String name) {
		String s = name;
		Matcher m = prefixNeutral.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}

		m = suffixMale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}

		m = prefixMale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}

		m = prefixFemale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}
		return s;
	}

	@Override
	public void transform() throws IOException {

		for (ComicCharacter cc : Repositories.COMIC_CHARACTERS.cache.values()) {
			Matcher prefixMaleMatcher = prefixMale.matcher(cc.name);
			Matcher suffixMaleMatcher = suffixMale.matcher(cc.name);
			Matcher prefixFemaleMatcher = prefixFemale.matcher(cc.name);

			String[] nameTokens = tokenize(removePrefixAndSuffix(cc.name));

			if (nameTokens.length == 0) {
				continue;
			}
			boolean isImpliedMale = impliedMale.matcher(cc.name).matches();
			boolean isImpliedFemale = impliedFemale.matcher(cc.name).matches();

			boolean isMale = personMatcher.isMaleName(nameTokens[0]);
			boolean isFemale = isMale ? false : (personMatcher
					.isFemaleName(nameTokens[0]));

			if (prefixMaleMatcher.matches()) {
				cc.honorificPrefix = prefixMaleMatcher.group(1).trim();
				cc.makeMale();
				if (oneName(nameTokens)) {
					if (isMale) {
						cc.givenName = nameTokens[0];
					} else {
						cc.familyName = nameTokens[0];
					}
				}
			} else if (suffixMaleMatcher.matches()) {
				cc.honorificSuffix = suffixMaleMatcher.group(1).trim();
				cc.makeMale();
			} else if (prefixFemaleMatcher.matches()) {
				cc.honorificPrefix = prefixFemaleMatcher.group(1).trim();
				cc.makeFemale();
				if (oneName(nameTokens)) {
					if (isFemale) {
						cc.givenName = nameTokens[0];
					} else {
						cc.familyName = nameTokens[0];
					}
				}
			} else if (isFemale || isImpliedFemale) {
				cc.makeFemale();
			} else if (isMale || isImpliedMale) {
				cc.makeMale();
			}

			if (nameTokens.length > 1) {
				Matcher isMaleName = personMatcher.maleNames(nameTokens[0]);
				if (isMaleName.matches()) {
					cc.makeMale();
					cc.givenName = isMaleName.group(1);
					if (firstAndLastName(nameTokens)) {
						cc.familyName = nameTokens[1];
					} else if (nameTokens.length == 3
							&& middleInitial.matcher(nameTokens[1]).matches()) {
						cc.familyName = nameTokens[2];
						if (Strings.isNullOrEmpty(cc.givenName)) {
							cc.givenName = nameTokens[0];
						}
					}
				}

				Matcher isFemaleName = personMatcher.femaleNames(nameTokens[0]);
				if (isFemaleName.matches()) {
					cc.makeFemale();
					cc.givenName = isFemaleName.group(1);
					if (firstAndLastName(nameTokens)) {
						cc.familyName = nameTokens[1];
					} else if (nameTokens.length == 3
							&& middleInitial.matcher(nameTokens[1]).matches()) {
						cc.familyName = nameTokens[2];
						if (Strings.isNullOrEmpty(cc.givenName)) {
							cc.givenName = nameTokens[0];
						}
					}
				}

				Matcher lastNamesMatcher = personMatcher
						.lastNames(nameTokens[nameTokens.length - 1]);
				if (lastNamesMatcher.matches()) {
					cc.familyName = lastNamesMatcher.group(1);
					if (nameTokens.length == 3
							&& middleInitial.matcher(nameTokens[1]).matches()
							&& Strings.isNullOrEmpty(cc.givenName)) {
						cc.givenName = nameTokens[0];
					}
				}
			}

			Matcher isPrefixNeutral = prefixNeutral.matcher(cc.name);
			if (isPrefixNeutral.matches()) {
				cc.honorificPrefix = HonorificExpander.expand(isPrefixNeutral
						.group(1).trim());
				if (oneName(nameTokens)) {
					if (isFemale || isMale) {
						if (Strings.isNullOrEmpty(cc.givenName)) {
							cc.givenName = nameTokens[0];
						}
					} else if (personMatcher.isLastName(nameTokens[0])) {
						if (Strings.isNullOrEmpty(cc.familyName)) {
							cc.familyName = nameTokens[0];
						}
					}
				} else {

				}
			}
		}
	}
}
