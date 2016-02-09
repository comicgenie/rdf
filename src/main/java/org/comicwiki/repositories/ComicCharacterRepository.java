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
package org.comicwiki.repositories;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.comicwiki.BaseRepository;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.model.ComicCharacter;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

public class ComicCharacterRepository extends BaseRepository<ComicCharacter> {

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
					"(^(ambassador|constable|senator|corporal|mayor|cap'n|professor|president|inspector|detective|doctor|dr|sgt|col|captain|major|maj|officer|general|gen|prof|capt|sheriff|lieutenant|lt)( |\\.){1})(.*)",
					Pattern.CASE_INSENSITIVE);

	private static Pattern suffixMale = Pattern.compile(
			".*(esq|esquire|caesar)\\.*$", Pattern.CASE_INSENSITIVE);

	private static String[] tokenize(String text) {
		return Iterables.toArray(Splitter.on(' ').trimResults()
				.omitEmptyStrings().split(text), String.class);
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

	private boolean oneName(String[] tokens) {
		return tokens.length == 1;
	}

	private boolean firstAndLastName(String[] tokens) {
		return tokens.length == 2;
	}

	public void addGender(PersonNameMatcher personMatcher) throws IOException {

		for (ComicCharacter cc : cache.values()) {
			Matcher prefixMaleMatcher = prefixMale.matcher(cc.name);
			Matcher suffixMaleMatcher = suffixMale.matcher(cc.name);
			Matcher prefixFemaleMatcher = prefixFemale.matcher(cc.name);

			String[] nameTokens = tokenize(removePrefixAndSuffix(cc.name));

			if(nameTokens.length == 0) {
				System.out.println(cc.name);
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
				cc.honorificPrefix = isPrefixNeutral.group(1).trim();
				if (oneName(nameTokens)) {
					if (isFemale || isMale) {
						if (Strings.isNullOrEmpty(cc.givenName)) {
							cc.givenName = nameTokens[0];
						}
					} else {
						if (Strings.isNullOrEmpty(cc.familyName)) {
							cc.familyName = nameTokens[0];
						}
					}
				} else {
					
				}
			}
		}
	}

	public String name(String name) throws IOException {
		String field[] = name.split("[ ]");
		InputStream modelIn = new FileInputStream("en-ner-person.bin");
		StringBuilder sb = new StringBuilder();
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);
			Span nameSpans[] = nameFinder.find(field);
			if (nameSpans == null || nameSpans.length == 0) {
				return "";
			}
			return field[nameSpans[nameSpans.length - 1].getEnd() - 1];
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}
}
