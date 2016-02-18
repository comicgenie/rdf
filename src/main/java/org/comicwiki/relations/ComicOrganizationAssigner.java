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
import java.util.stream.Stream;

import org.comicwiki.IRI;
import org.comicwiki.IRICache;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;

public final class ComicOrganizationAssigner {

	private final ComicOrganization organization;

	private final IRICache iriCache;

	public ComicOrganizationAssigner(ComicOrganization organization,
			IRICache iriCache) {
		this.organization = organization;
		this.iriCache = iriCache;
	}

	/**
	 * ComicOrganization -> ComicCharacters* ComicCharacters ->
	 * ComicOrganization
	 */
	public void characters(Collection<ComicCharacter> characters) {
		characters.forEach(cc -> {
			cc.memberOf.add(IRI.create(organization.instanceId, iriCache));
			organization.members.add(IRI.create(cc.instanceId, iriCache));
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

		creators.forEach(c -> c.workedOn.add(IRI.create(
				organization.instanceId, iriCache)));
		colors.forEach(e -> organization.creativeWork.colorists.add(IRI.create(
				e.instanceId, iriCache)));
		inks.forEach(e -> organization.creativeWork.inkers.add(IRI.create(
				e.instanceId, iriCache)));
		letters.forEach(e -> organization.creativeWork.letterers.add(IRI
				.create(e.instanceId, iriCache)));
		pencils.forEach(e -> organization.creativeWork.pencilers.add(IRI
				.create(e.instanceId, iriCache)));
		script.forEach(e -> organization.creativeWork.authors.add(IRI.create(
				e.instanceId, iriCache)));
		editors.forEach(e -> organization.creativeWork.editors.add(IRI.create(
				e.instanceId, iriCache)));

	}

	/**
	 * ComicOrganization -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		genres.forEach(g -> organization.creativeWork.genres.add(g.instanceId));
	}

	/**
	 * ComicOrganization -> ComicStory
	 */
	public void story(ComicStory story) {
		story.organizations.add(IRI.create(organization.instanceId, iriCache));
	}
}
