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

import org.comicwiki.IRICache;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.CreatorRole;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;

public final class ComicCreatorAssigner {
	private final Collection<Person> colors;
	private final Stream<Person> creators;
	private final Collection<Person> editors;
	private final Collection<Person> inks;
	private final Collection<Person> letters;
	private final Collection<Person> pencils;
	private final Collection<Person> script;

	public ComicCreatorAssigner(Collection<Person> colors,
			Collection<Person> inks, Collection<Person> letters,
			Collection<Person> pencils, Collection<Person> script,
			Collection<Person> editors) {;
		this.colors = colors;
		this.inks = inks;
		this.letters = letters;
		this.pencils = pencils;
		this.script = script;
		this.editors = editors;
		creators = Stream.of(colors, inks, letters, pencils, script, editors)
				.flatMap(Collection::stream);
	}

	/**
	 * ComicStory.[inkers][....] -> ComicCharacters ComicCharacters ->
	 * ComicStory.[inkers][pencilers][....]
	 */
	public void characters(Collection<ComicCharacter> characters) {
		characters.forEach(cc -> {
			colors.forEach(e -> cc.creativeWork.colorists.add(
					e.instanceId ));
			inks.forEach(e -> cc.creativeWork.inkers.add(
					e.instanceId ));
			letters.forEach(e -> cc.creativeWork.letterers.add(
					e.instanceId ));
			pencils.forEach(e -> cc.creativeWork.pencilers.add(
					e.instanceId ));
			script.forEach(e -> cc.creativeWork.authors.add(
					e.instanceId ));
			editors.forEach(e -> cc.creativeWork.editors.add(
					e.instanceId ));
			creators.forEach(c -> {
				c.workedOn.add(cc.instanceId );
			});
		});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory.[inkers][....]
	 */
	public void colleagues() {
		creators.forEach(one -> {
			creators.forEach(two -> {
				if (!one.equals(two)) {
					one.colleagues.add(two.instanceId );
				}
			});
		});
	}

	public void jobTitles() {
		colors.forEach(e -> e.jobTitle.add(CreatorRole.colorist.name()));
		inks.forEach(e -> e.jobTitle.add(CreatorRole.inker.name()));
		letters.forEach(e -> e.jobTitle.add(CreatorRole.letterist.name()));
		pencils.forEach(e -> e.jobTitle.add(CreatorRole.penclier.name()));
		script.forEach(e -> e.jobTitle.add(CreatorRole.writer.name()));
		editors.forEach(e -> e.jobTitle.add(CreatorRole.editor.name()));
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		genres.forEach(g -> {
			creators.forEach(c -> c.areasWorkedIn.add(g.instanceId
					));// TODO:
			// STRING
			// OR
			// IRI
		});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicOrganizations
	 */
	public void organizations(Collection<ComicOrganization> organizations) {
		organizations.forEach(e -> {
			creators.forEach(c -> {
				c.workedOn.add(e.instanceId );
			});
		});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory
	 */
	public void story(ComicStory story) {
		colors.forEach(e -> story.colorists.add(e.instanceId));
		inks.forEach(e -> story.inkers.add(e.instanceId));
		letters.forEach(e -> story.letterers.add(e.instanceId));
		pencils.forEach(e -> story.pencilers.add(e.instanceId));
		script.forEach(e -> story.authors.add(e.instanceId ));
		editors.forEach(e -> story.editors.add(e.instanceId));
	}
}
