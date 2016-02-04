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
package org.comicwiki.gcdb.relations;

import java.util.Collection;
import java.util.stream.Stream;

import org.comicwiki.gcdb.CreatorRole;
import org.comicwiki.gcdb.ResourceUtils;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.ComicStory;
import org.comicwiki.model.schema.Person;

import static org.comicwiki.gcdb.KeyUtils.*;

public class ComicCreatorAssigner {
	private Collection<Person> colors;
	private Collection<Person> inks;
	private Collection<Person> letters;
	private Collection<Person> pencils;
	private Collection<Person> script;
	private Stream<Person> creators;

	/**
	 * ComicStory.[inkers][....] -> ComicOrganizations 
	 */
	void organizations(Collection<ComicOrganization> organizations) {
		organizations.forEach(e -> {
			String key = readKey(e);
			creators.forEach(c -> {
				c.workedOn.add(key);
			});
		});
	}

	
	public ComicCreatorAssigner(Collection<Person> colors,
			Collection<Person> inks, Collection<Person> letters,
			Collection<Person> pencils, Collection<Person> script) {
		this.colors = colors;
		this.inks = inks;
		this.letters = letters;
		this.pencils = pencils;
		this.script = script;

		creators = Stream.of(colors, inks, letters, pencils, script).flatMap(
				Collection::stream);
	}
	
	/**
	 * ComicStory.[inkers][....] -> ComicStory.genres
	 */
	void genres(Collection<Genre> genres) {
		genres.forEach(g -> {
			String key = readKey(g);
			creators.forEach(c -> c.areasWorkedIn.add(key));
		});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicCharacters
	 * ComicCharacters -> ComicStory.[inkers][pencilers][....]
	 */
	void characters(Collection<ComicCharacter> characters) {
			characters.forEach(cc -> {
				colors.forEach(e -> cc.creativeWork.colorists.add(readKey(e)));
				inks.forEach(e -> cc.creativeWork.inkers.add(readKey(e)));
				letters.forEach(e -> cc.creativeWork.letterers.add(readKey(e)));
				pencils.forEach(e -> cc.creativeWork.pencilers.add(readKey(e)));
				script.forEach(e -> cc.creativeWork.authors.add(readKey(e)));
				
				creators.forEach(c -> {
					c.workedOn.add(readKey(cc));
				});
			});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory
	 */
	void story(ComicStory story) {
		colors.forEach(e -> story.colorists.add(readKey(e)));
		inks.forEach(e -> story.inkers.add(readKey(e)));
		letters.forEach(e -> story.letterers.add(readKey(e)));
		pencils.forEach(e -> story.pencilers.add(readKey(e)));
		script.forEach(e -> story.authors.add(readKey(e)));
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory.[inkers][....]
	 */
	 void colleagues() {
		creators.forEach(one -> {
			String oneKey = readKey(one);
			if (oneKey != null) {
				creators.forEach(two -> {
					String twoKey = readKey(two);
					if (!oneKey.equals(twoKey)) {
						one.colleagues.add(ResourceUtils.expandIri(twoKey));
					}
				});
			}
		});
	}

	public void execute() {
		colleagues();
		colors.forEach(e -> e.jobTitle.add(CreatorRole.colorist.name()));
		inks.forEach(e -> e.jobTitle.add(CreatorRole.inker.name()));
		letters.forEach(e -> e.jobTitle.add(CreatorRole.letterist.name()));
		pencils.forEach(e -> e.jobTitle.add(CreatorRole.penclier.name()));
		script.forEach(e -> e.jobTitle.add(CreatorRole.writer.name()));
	}
}
