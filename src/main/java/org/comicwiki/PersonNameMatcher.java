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
package org.comicwiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;

public class PersonNameMatcher {

	public HashMap<String, String> cache = new HashMap<>();
	
	public HashSet<String> maleCache = new HashSet<>();
	
	public HashSet<String> femaleCache = new HashSet<>();
	
	public HashSet<String> lastNamesCache = new HashSet<>();

	private Pattern patternLastNames;

	private Pattern patternMaleNames;

	private Pattern patternFemaleNames;
	
	public boolean isMaleName(String name) {
		return maleNames(name).matches();
	}
	
	public boolean isFemaleName(String name) {
		return femaleNames(name).matches();
	}
	
	public Matcher femaleNames(String text) {
		return patternFemaleNames.matcher(text);
	}
	
	public Matcher maleNames(String text) {
		return patternMaleNames.matcher(text);
	}
	
	public Matcher lastNames(String text) {
		return patternLastNames.matcher(text);
	}
	
	public void load(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = in.readLine()) != null) {
			String[] tokens = line.split(",");
			if(cache.containsKey(tokens[0])) {
				cache.remove(tokens[0]);
			} else {
				cache.put(tokens[0], tokens[1]);
			}
		}
		
		for(Entry<String, String> e : cache.entrySet()) {
			if(e.getValue().equals("M")) {
				maleCache.add(e.getKey());
			} else {
				femaleCache.add(e.getKey());
			}
		}
		in.close();
		
		patternMaleNames = Pattern.compile(
				".*\\b(" + Joiner.on("|").join(maleCache.toArray()) + ")\\b.*",
				Pattern.CASE_INSENSITIVE);
		patternFemaleNames = Pattern.compile(
				".*\\b(" + Joiner.on("|").join(femaleCache.toArray())
						+ ")\\b.*", Pattern.CASE_INSENSITIVE);
		
	}
	
	public void loadLastNames(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = in.readLine()) != null) {
			lastNamesCache.add(line);
		}
		in.close();
		patternLastNames = Pattern.compile(".*\\b("
				+ Joiner.on("|").join(lastNamesCache.toArray()) + ")\\b.*",
				Pattern.CASE_INSENSITIVE);
	}
}
