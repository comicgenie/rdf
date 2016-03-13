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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;

public final class ComicCharactersAssigner {

	private final Collection<ComicCharacter> comicCharacters;

	/**
	 * ComicCharacters -> ComicCharacters
	 */
	public void colleagues() {
		ComicCharacter[] charactersArray = comicCharacters
				.toArray(new ComicCharacter[comicCharacters.size()]);
		for (Person one : charactersArray) {
			for (Person two : charactersArray) {
				if (!one.equals(two)) {
					one.addColleague(two.instanceId);
				}
			}
		}
	}

	public ComicCharactersAssigner(Collection<ComicCharacter> comicCharacters) {
		this.comicCharacters = comicCharacters;
		comicCharacters.forEach(c -> {
			checkNotNull(c.instanceId, "ComicCharacter.instanceId: " + c.name);
		});
	}

	/**
	 * ComicCharacters -> ComicStory
	 */
	public void story(ComicStory story) {
		comicCharacters.forEach(e -> story.addCharacter(e.instanceId));
	}

	/**
	 * ComicCharacters -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		genres.forEach(g -> comicCharacters.forEach(c -> c.creativeWork
				.addGenre(g.instanceId)));
	}
}
