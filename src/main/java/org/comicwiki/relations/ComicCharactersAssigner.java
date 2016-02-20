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

import java.util.Collection;

import org.comicwiki.IRI;
import org.comicwiki.IRICache;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.bib.ComicStory;

public final class ComicCharactersAssigner {

	private final Collection<ComicCharacter> comicCharacters;
	private final IRICache iriCache;

	/**
	 * ComicCharacters -> ComicCharacters
	 */
	public void colleagues() {
		comicCharacters.forEach(one -> {
			comicCharacters.forEach(two -> {
				if (!two.equals(one)) {
					one.colleagues.add(two.instanceId);
				}
			});
		});
	}

	// Just put in characters in one team
	public ComicCharactersAssigner(Collection<ComicCharacter> comicCharacters, IRICache iriCache) {
		this.comicCharacters = comicCharacters;
		this.iriCache = iriCache;
	}

	/**
	 * ComicCharacters -> ComicStory
	 */
	public void story(ComicStory story) {
		comicCharacters.forEach(e -> story.characters.add(e.instanceId));
	}

	/**
	 * ComicCharacters -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		genres.forEach(g -> comicCharacters.forEach(c -> c.creativeWork.genres
				.add(g.name)));
	}
}
