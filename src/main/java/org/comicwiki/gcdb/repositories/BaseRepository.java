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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.comicwiki.gcdb.KeyUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseRepository<T> {

	protected final HashMap<String, T> cache = new HashMap<>();

	protected ObjectMapper mapper = new ObjectMapper();

	public boolean contains(String id) {
		return cache.containsKey(id);
	}
	
	public Collection<T> find(Collection<ComicMatcher<T>> matchers) {
		if(matchers == null) {
			return cache.values();
		}
		
		Collection<T> items = new HashSet<T>();
		for(T item : cache.values()) {
			for(ComicMatcher<T> matcher : matchers) {
				if(!matcher.match(item)) {
					continue;
				} 
				items.add(item);
			}
		}
		return items;
	}
	
	public void add(T item) {
		String key = KeyUtils.readKey(item);
		if(contains(key)) {
			merge(item, getById(key));
		} else {
			cache.put(key, item);
		}		
	}

	public void add(Collection<T> items) {
		for (T item : items) {
			add(item);
		}
	}
	
	public abstract T merge(T source, T target);

	public T getById(String id) {
		return cache.get(id);
	}

	public void save(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		RepositoryFile<T> repoFile = new RepositoryFile<T>();
		repoFile.values = cache.values();
		mapper.setSerializationInclusion(Include.NON_DEFAULT);
		mapper.writeValue(fos, repoFile);
		fos.close();
	}

	public void load(File file) throws IOException {
		RepositoryFile<T> repoFile = mapper.readValue(file,
				RepositoryFile.class);
		Collection<T> values = repoFile.values;
		for (T value : values) {
			add(value);
		}
	}
	
	public void print() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, cache.values());
	}
	
	public abstract void load() throws IOException;

}
