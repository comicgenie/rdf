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

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.ComicStory;
import org.comicwiki.model.schema.Person;

import static org.comicwiki.gcdb.KeyUtils.*;

public class ComicOrganizationAssigner {
	

	private ComicOrganization organization;
	private String organizationKey;

	/**
	 * ComicOrganization -> ComicStory.[inkers][pencilers][....]
	 */
	void creators(Collection<Person> colors,
			Collection<Person> inks, Collection<Person> letters,
			Collection<Person> pencils, Collection<Person> script) {

		Stream<Person> creators = Stream.of(colors, inks, letters, pencils, script).flatMap(
				Collection::stream);
		
		creators.forEach(c -> {
			c.workedOn.add(organizationKey);
		});
		
		colors.forEach(e -> organization.creativeWork.colorists.add(readKey(e)));
		inks.forEach(e -> organization.creativeWork.inkers.add(readKey(e)));
		letters.forEach(e -> organization.creativeWork.letterers.add(readKey(e)));
		pencils.forEach(e -> organization.creativeWork.pencilers.add(readKey(e)));
		script.forEach(e -> organization.creativeWork.authors.add(readKey(e)));
		
	}

	/**
	 * ComicOrganization -> ComicStory
	 */
	void story(ComicStory story) {
		story.organizations.add(organizationKey);
	}

	/**
	 * ComicOrganization -> ComicStory.genres
	 */
	void genres(Collection<Genre> genres) {
		genres.forEach(g -> {
			String key = readKey(g);
			organization.creativeWork.genres.add(key);
		});
	}

	/**
	 * ComicOrganization -> ComicCharacters*
	 * ComicCharacters -> ComicOrganization
	 */
	void characters(Collection<ComicCharacter> characters) {
		characters.forEach(cc -> {
			cc.memberOf.add(organizationKey);
			organization.members.add(readKey(cc));
		});
	}

	public ComicOrganizationAssigner(ComicOrganization organization) {
		this.organization = organization;
		this.organizationKey = readKey(organization);
	}
}
