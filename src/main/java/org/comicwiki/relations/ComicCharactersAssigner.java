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
package org.comicwiki.relations;

import static org.comicwiki.KeyUtils.readKey;

import java.util.Collection;

import org.comicwiki.ResourceUtils;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.ComicStory;

public class ComicCharactersAssigner {

	private Collection<ComicCharacter> comicCharacters;
	
	/**
	 * ComicCharacters -> ComicCharacters
	 */
	 void colleagues() {
		 comicCharacters.forEach(one -> {
			String oneKey = readKey(one);
			if (oneKey != null) {
				comicCharacters.forEach(two -> {
					String twoKey = readKey(two);
					if (!oneKey.equals(twoKey)) {
						one.colleagues.add(ResourceUtils.expandIri(twoKey));
					}
				});
			}
		});
	}
	 
	//Just put in characters in one team
	public ComicCharactersAssigner(Collection<ComicCharacter> comicCharacters)  {
		this.comicCharacters = comicCharacters;
	}
	
	/**
	 * ComicCharacters -> ComicStory
	 */
	void story(ComicStory story) {	
		comicCharacters.forEach(e -> story.characters.add(readKey(e)));
	}
	
	/**
	 * ComicCharacters -> ComicStory.genres
	 */
	void genres(Collection<Genre> genres) {
		genres.forEach(g -> {
			String key = readKey(g);
			comicCharacters.forEach(c -> c.creativeWork.genres.add(key));
		});
	}
	
	
}
