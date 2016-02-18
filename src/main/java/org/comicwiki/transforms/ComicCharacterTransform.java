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
package org.comicwiki.transforms;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.comicwiki.HonorificExpander;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.RepositoryTransform;
import org.comicwiki.model.schema.Person;

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
			"(.*){1}(esq|esquire|caesar)\\.*$", Pattern.CASE_INSENSITIVE);

	private static void extractNames(Person person, String[] nameTokens) {
		if (firstAndLastName(nameTokens)) {
			person.familyName = nameTokens[1];
		} else if (nameTokens.length == 3
				&& middleInitial.matcher(nameTokens[1]).matches()) {
			person.familyName = nameTokens[2];
		}
	}

	private static boolean firstAndLastName(String[] tokens) {
		return tokens.length == 2;
	}

	private static boolean oneName(String[] tokens) {
		return tokens.length == 1;
	}

	private static void oneNameWithGender(Person person, String[] nameTokens,
			boolean hasGender) {
		if (oneName(nameTokens)) {
			if (hasGender) {
				person.givenName = nameTokens[0];
			} else {
				person.familyName = nameTokens[0];
			}
		}
	}

	private static String[] tokenize(String text) {
		return Iterables.toArray(Splitter.on(' ').trimResults()
				.omitEmptyStrings().split(text), String.class);
	}

	private final PersonNameMatcher personMatcher;

	private final Repositories repositories;

	public ComicCharacterTransform(PersonNameMatcher personMatcher,
			Repositories repositories) {
		this.personMatcher = personMatcher;
		this.repositories = repositories;
	}

	protected String removePrefixAndSuffix(String name) {
		checkNotNull(name, "name");
		String s = name;
		Matcher m = prefixNeutral.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}

		m = suffixMale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount() - 1);
		}

		m = prefixMale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}

		m = prefixFemale.matcher(s);
		if (m.matches()) {
			s = m.group(m.groupCount());
		}
		return s.trim();
	}

	@Override
	public void transform() throws IOException {
		for (Person person : repositories.COMIC_CHARACTERS.cache.values()) {
			if (Strings.isNullOrEmpty(person.name)) {
				continue;
			}

			String[] nameTokens = tokenize(removePrefixAndSuffix(person.name));
			if (nameTokens.length == 0) {
				continue;
			}

			Matcher prefixMaleMatcher = prefixMale.matcher(person.name);
			Matcher suffixMaleMatcher = suffixMale.matcher(person.name);
			Matcher prefixFemaleMatcher = prefixFemale.matcher(person.name);
			Matcher prefixNeutralMatcher = prefixNeutral.matcher(person.name);

			boolean isImpliedMale = impliedMale.matcher(person.name).matches();
			boolean isImpliedFemale = impliedFemale.matcher(person.name)
					.matches();

			boolean isMale = personMatcher.isMaleName(nameTokens[0]);
			boolean isFemale = isMale ? false : (personMatcher
					.isFemaleName(nameTokens[0]));

			if (prefixMaleMatcher.matches()) {
				person.honorificPrefix = prefixMaleMatcher.group(1).trim();
				person.makeMale();
				oneNameWithGender(person, nameTokens, isMale);
			} else if (suffixMaleMatcher.matches()) {
				person.honorificSuffix = suffixMaleMatcher.group(1).trim();
				person.makeMale();
				oneNameWithGender(person, nameTokens, isMale);
			} else if (prefixFemaleMatcher.matches()) {
				person.honorificPrefix = prefixFemaleMatcher.group(1).trim();
				person.makeFemale();
				oneNameWithGender(person, nameTokens, isFemale);
			} else if (prefixNeutralMatcher.matches()) {
				person.honorificPrefix = HonorificExpander
						.expand(prefixNeutralMatcher.group(1).trim());
				if (oneName(nameTokens)) {
					if (isFemale || isMale) {
						person.givenName = nameTokens[0];
					} else if (personMatcher.isLastName(nameTokens[0])) {
						person.familyName = nameTokens[0];
					}
				}
			} else if (isFemale || isImpliedFemale) {
				person.makeFemale();
				if (oneName(nameTokens)) {
					person.givenName = nameTokens[0];
				}
			} else if (isMale || isImpliedMale) {
				person.makeMale();
				if (oneName(nameTokens)) {
					person.givenName = nameTokens[0];
				}
			} else if (oneName(nameTokens)) {// one name, no gender
				Matcher lastNamesMatcher = personMatcher
						.lastNames(nameTokens[0]);
				if (lastNamesMatcher.matches()) {
					person.familyName = nameTokens[0];
				}
			}

			if (nameTokens.length > 1) {
				Matcher isMaleName = personMatcher.maleNames(nameTokens[0]);
				if ("M".equals(person.gender) && isMaleName.matches()) {
					person.givenName = isMaleName.group(1);
					extractNames(person, nameTokens);
				}

				Matcher isFemaleName = personMatcher.femaleNames(nameTokens[0]);
				if ("F".equals(person.gender) && isFemaleName.matches()) {
					person.givenName = isFemaleName.group(1);
					extractNames(person, nameTokens);
				}

				Matcher lastNamesMatcher = personMatcher
						.lastNames(nameTokens[nameTokens.length - 1]);
				if (lastNamesMatcher.matches()) {
					person.familyName = lastNamesMatcher.group(1);
					if (nameTokens.length == 3 && Strings.isNullOrEmpty(person.givenName)) {
						if (middleInitial.matcher(nameTokens[1]).matches()) {
							person.givenName = nameTokens[0];
						} else {
							person.givenName = nameTokens[1];
							isMaleName = personMatcher.maleNames(nameTokens[1]);
							isFemaleName = personMatcher.femaleNames(nameTokens[1]);
							if(isMaleName.matches()) {
								person.makeMale();
							} else if(isFemaleName.matches()) {
								person.makeFemale();
							}
						}
					}
				}
			}
		}
	}
}
