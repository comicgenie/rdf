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
package org.comicwiki.gcdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class NamesImporter {

	public HashMap<String, String> cache = new HashMap<>();
	
	public HashSet<String> maleCache = new HashSet<>();
	
	public HashSet<String> femaleCache = new HashSet<>();
	
	public HashSet<String> lastNamesCache = new HashSet<>();
	
	public void load2(File file) throws IOException {
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
		
		in.close();
		System.out.println(cache);
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
	}
	
	public void loadLastNames(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = in.readLine()) != null) {
			lastNamesCache.add(line);
		}
		in.close();
	}
}
