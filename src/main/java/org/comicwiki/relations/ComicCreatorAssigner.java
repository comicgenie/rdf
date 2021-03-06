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
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.CreatorRole;
import org.comicwiki.model.Genre;
import org.comicwiki.model.schema.Organization;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.bib.ComicStory;

import com.google.common.collect.Sets;

public final class ComicCreatorAssigner {
	private final Collection<Person> colors;
	private final Collection<Person> editors;
	private final Collection<Person> inks;
	private final Collection<Person> letters;
	private final Collection<Person> pencils;
	private final Collection<Person> script;
	private Collection<Person> creators;

	public ComicCreatorAssigner(Collection<Person> colors,
			Collection<Person> inks, Collection<Person> letters,
			Collection<Person> pencils, Collection<Person> script,
			Collection<Person> editors) {

		creators = Sets.newHashSet();
		if (colors == null) {
			colors = Sets.newHashSet();
		}
		if (inks == null) {
			inks = Sets.newHashSet();
		}
		if (letters == null) {
			letters = Sets.newHashSet();
		}
		if (pencils == null) {
			pencils = Sets.newHashSet();
		}
		if (script == null) {
			script = Sets.newHashSet();
		}
		if (editors == null) {
			editors = Sets.newHashSet();
		}
		creators.addAll(colors);
		creators.addAll(inks);
		creators.addAll(letters);
		creators.addAll(pencils);
		creators.addAll(script);
		creators.addAll(editors);

		creators.forEach(c -> checkNotNull(c.instanceId,
				"creator instanceId is null: " + c.name));
		this.colors = colors;
		this.inks = inks;
		this.letters = letters;
		this.pencils = pencils;
		this.script = script;
		this.editors = editors;
	}

	/**
	 * ComicStory.[inkers][....] -> ComicCharacters ComicCharacters ->
	 * ComicStory.[inkers][pencilers][....]
	 */
	public void characters(Collection<ComicCharacter> characters) {
		if (characters == null) {
			return;
		}
		characters
				.forEach(cc -> {
					checkNotNull(cc.instanceId, "ComicCharacter.instanceId: "
							+ cc.name);
					colors.forEach(e -> cc.creativeWork
							.addColorist(e.instanceId));
					inks.forEach(e -> cc.creativeWork.addInker(e.instanceId));
					letters.forEach(e -> cc.creativeWork
							.addLetter(e.instanceId));
					pencils.forEach(e -> cc.creativeWork
							.addPenciler(e.instanceId));
					script.forEach(e -> cc.creativeWork.addAuthor(e.instanceId));
					editors.forEach(e -> cc.creativeWork
							.addEditor(e.instanceId));
					creators.forEach(c -> c.addWorkedOn(cc.instanceId));
				});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory.[inkers][....]
	 */
	public void colleagues() {
		Person[] creatorsArray = creators.toArray(new Person[creators.size()]);
		for (Person one : creatorsArray) {
			for (Person two : creatorsArray) {
				if (!one.equals(two)) {
					one.addColleague(two.instanceId);
				}
			}
		}
	}

	public void jobTitles() {
		colors.forEach(e -> e.addJobTitle(CreatorRole.colorist.name()));
		inks.forEach(e -> e.addJobTitle(CreatorRole.inker.name()));
		letters.forEach(e -> e.addJobTitle(CreatorRole.letterist.name()));
		pencils.forEach(e -> e.addJobTitle(CreatorRole.penciller.name()));
		script.forEach(e -> e.addJobTitle(CreatorRole.writer.name()));
		editors.forEach(e -> e.addJobTitle(CreatorRole.editor.name()));
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory.genres
	 */
	public void genres(Collection<Genre> genres) {
		genres.forEach(g -> creators.forEach(c -> c
				.addAreasWorkedIn(g.instanceId)));
	}

	/**
	 * ComicStory.[inkers][....] -> ComicOrganizations
	 */
	public void comicOrganizations(Collection<ComicOrganization> organizations) {
		if (organizations == null) {
			return;
		}
		organizations.forEach(e -> {
			checkNotNull(e.instanceId, "ComicOrganization.instanceId: "
					+ e.name);
			creators.forEach(c -> c.addWorkedOn(e.instanceId));
		});
	}

	public void organization(Organization organization) {
		if (organization == null) {
			return;
		}
		checkNotNull(organization.instanceId, "Organization.instanceId: "
				+ organization.name);
		creators.forEach(c -> {
			c.addWorksFor(organization.instanceId);
			organization.addMembers(c.instanceId);
		});
	}

	/**
	 * ComicStory.[inkers][....] -> ComicStory
	 */
	public void story(ComicStory story) {
		colors.forEach(e -> story.addColorist(e.instanceId));
		inks.forEach(e -> story.addInker(e.instanceId));
		letters.forEach(e -> story.addLetter(e.instanceId));
		pencils.forEach(e -> story.addPenciler(e.instanceId));
		script.forEach(e -> story.addAuthor(e.instanceId));
		editors.forEach(e -> story.addEditor(e.instanceId));
	}
}
