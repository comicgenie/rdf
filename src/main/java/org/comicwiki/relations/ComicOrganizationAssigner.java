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
import java.util.stream.Stream;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;

public final class ComicOrganizationAssigner {

	private final ComicOrganization organization;

	public ComicOrganizationAssigner(ComicOrganization organization) {
		checkNotNull(organization.instanceId, "ComicOrganization.instanceId: "
				+ organization.name);
		this.organization = organization;
	}

	/**
	 * ComicOrganization -> ComicCharacters* ComicCharacters ->
	 * ComicOrganization
	 */
	public void characters(Collection<ComicCharacter> characters) {
		characters
				.forEach(cc -> {
					checkNotNull(cc.instanceId, "ComicCharacter.instanceId: "
							+ cc.name);
					cc.memberOf.add(organization.instanceId);
					organization.members.add(cc.instanceId);
				});
	}

	/**
	 * ComicOrganization -> ComicStory.[inkers][pencilers][....]
	 */
	public void creators(Collection<Person> colors, Collection<Person> inks,
			Collection<Person> letters, Collection<Person> pencils,
			Collection<Person> script, Collection<Person> editors) {

		Stream<Person> creators = Stream.of(colors, inks, letters, pencils,
				script, editors).flatMap(Collection::stream);
		
		creators.forEach(c -> {
			checkNotNull(c.instanceId, "creator instanceId is null: " + c.name);
		});
		
		creators = Stream.of(colors, inks, letters, pencils,
				script, editors).flatMap(Collection::stream);
		creators.forEach(c -> c.workedOn.add(organization.instanceId));
		colors.forEach(e -> organization.creativeWork.colorists
				.add(e.instanceId));
		inks.forEach(e -> organization.creativeWork.inkers.add(e.instanceId));
		letters.forEach(e -> organization.creativeWork.letterers
				.add(e.instanceId));
		pencils.forEach(e -> organization.creativeWork.pencilers
				.add(e.instanceId));
		script.forEach(e -> organization.creativeWork.authors.add(e.instanceId));
		editors.forEach(e -> organization.creativeWork.editors
				.add(e.instanceId));

	}

	/**
	 * ComicOrganization -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		// TODO: String or IRI for genres?
		genres.forEach(g -> organization.creativeWork.genres
				.add(g.instanceId.value));
	}

	/**
	 * ComicOrganization -> ComicStory
	 */
	public void story(ComicStory story) {
		story.organizations.add(organization.instanceId);
	}
}
