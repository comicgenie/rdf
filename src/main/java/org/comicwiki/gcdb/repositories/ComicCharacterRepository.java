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
package org.comicwiki.gcdb.repositories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.comicwiki.model.ComicCharacter;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import com.google.common.base.Joiner;

public class ComicCharacterRepository extends BaseRepository<ComicCharacter> {

	@Override
	public ComicCharacter merge(ComicCharacter source, ComicCharacter target) {
		return target;
	}

	@Override
	public void load() throws IOException {

	}
	public String name(String name) throws IOException {
		String field [] = name.split("[ ]");
		InputStream modelIn = new FileInputStream("en-ner-person.bin");
		StringBuilder sb = new StringBuilder();
		try {
		  TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
		  NameFinderME nameFinder = new NameFinderME(model);
		  //String[] field = new String[]{"George", "M.", "Cohan"};
		  Span nameSpans[] = nameFinder.find(field);
		  if(nameSpans == null || nameSpans.length == 0 ) {
			  return "";
		  }
		  return field[nameSpans[nameSpans.length - 1].getEnd() - 1];
		  /*
		  for(Span span : nameSpans) {
			  
			  for(int i = span.getStart() ; i < span.getEnd(); i++) {
				  sb.append(field[i]);
				 // System.out.println(field[i]);
			  }
			  
		  }
		  */
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		return sb.toString();
	}
	
	public void addGender(Collection<String> m, Collection<String> f, Collection<String> lastNames) throws IOException {
		FileOutputStream charactersFile = new FileOutputStream("character.txt");
		System.out.println("TOTAL:" + cache.size());
		Pattern malePrefix = Pattern
				.compile(
						"^(mr|sir|lord|master|king|prince|count|duke|baron|viscount|knight|earl|emperor)( |\\.){1}.*",
						Pattern.CASE_INSENSITIVE);
		Pattern maleSuffix = Pattern.compile(".*(esq|esquire|caesar)\\.*$",
				Pattern.CASE_INSENSITIVE);
		Pattern femalePrefix = Pattern
				.compile(
						"^(mrs|miss|ms|lady|princess|queen|dame|madam|ma'am|duchess|viscountess|countess|baroness)( |\\.){1}.*",
						Pattern.CASE_INSENSITIVE);
		Pattern femaleReference = Pattern.compile(".*(girl|woman).*",
				Pattern.CASE_INSENSITIVE);
		Pattern maleReference = Pattern.compile(".*\\b(man)\\b",
				Pattern.CASE_INSENSITIVE);
		Pattern honorific = Pattern.compile(".*(mayor|professor|inspector|detective|doctor|dr|sgt|col|captain|major|general|prof|capt|sheriff|gen|ancle|aunt|lt)\\.*$",
				Pattern.CASE_INSENSITIVE);
		
		//Professor/Inspector
		String mj = Joiner.on("|").join(m.toArray());
		String fj = Joiner.on("|").join(f.toArray());
		String ln = Joiner.on("|").join(lastNames.toArray());
		
		Pattern patternM = Pattern.compile(".*\\b(" + mj + ")\\b.*", Pattern.CASE_INSENSITIVE);
		Pattern patternF = Pattern.compile(".*\\b(" + fj + ")\\b.*", Pattern.CASE_INSENSITIVE);
		Pattern patternLN = Pattern.compile(".*\\b(" + ln + ")\\b.*", Pattern.CASE_INSENSITIVE);
		
		int c = 0;
		int male = 0;
		int female = 0;
		int countLast = 0;
		int honorCount = 0;
		int familyCount = 0;
		for (ComicCharacter cc : cache.values()) {
			
			//charactersFile.write( (cc.name+"\r\n").getBytes());
			Matcher malePrefixMatcher = malePrefix.matcher(cc.name);
			Matcher suffixPrefixMatcher = maleSuffix.matcher(cc.name);
			Matcher femalePrefixMatcher = femalePrefix.matcher(cc.name);

			Matcher maleMatcher = maleReference.matcher(cc.name);
			Matcher femaleMatcher = femaleReference.matcher(cc.name);

			if (malePrefixMatcher.matches()) {
				c++;
				cc.honorificPrefix = malePrefixMatcher.group(1);
				cc.gender = "M";
			} else if (suffixPrefixMatcher.matches()) {
				c++;
				cc.honorificSuffix = suffixPrefixMatcher.group(1);
				cc.gender = "M";
			} else if (femalePrefixMatcher.matches()) {
				c++;
				cc.honorificSuffix = femalePrefixMatcher.group(1);
				cc.gender = "F";
			} else if (femaleMatcher.matches()) {
				c++;
				cc.gender = "F";
			} else if (maleMatcher.matches()) {
				c++;
				cc.gender = "M";
			}
			Matcher lnm = patternLN.matcher(cc.name);
			if(lnm.matches()) {
				countLast++;
				cc.familyName = lnm.group(1);
				String[] tokens = cc.name.split("[ ]");
				/*
				if(tokens.length == 2) {
					familyCount++;
					cc.givenName = tokens[0];
				}
				*/
			}
			
			Matcher mmm = patternM.matcher(cc.name);
			if (mmm.matches()) {
				male++;
				cc.gender = "M";
				cc.givenName = mmm.group(1);
				String[] tokens = cc.name.split("[ ]");
				/*
				if(tokens.length == 2) {
					familyCount++;
					cc.familyName = tokens[0];
				}
				*/
			}

			Matcher mmf = patternF.matcher(cc.name);
			if (mmf.matches()) {
				female++;
				cc.gender = "F";
				cc.givenName = mmf.group(1);
			//	cc.familyName = name(cc.name);
			//	System.out.println(cc.name + " : " + cc.givenName + "," + cc.familyName);
				/*
				String[] tokens = cc.name.split("[ ]");
				
				if(tokens.length == 2) {
					familyCount++;
					cc.familyName = tokens[0];
				}
				*/
			}
			
			Matcher mmh = honorific.matcher(cc.name);
			if(mmh.matches()) {
				honorCount++;
				cc.honorificPrefix = mmh.group(1);
			}
		}
		System.out.println("------Gender Count:" + c + ":" + male + ":"
				+ female + ":" + countLast +":" + honorCount +":"+ familyCount);
		charactersFile.close();

	}

}
