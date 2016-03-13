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
package org.comicwiki.model.schema;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/Person", key = "name")
public class Person extends Thing {

	@Predicate("areasWorkedIn")
	@SchemaComicWiki
	@ObjectIRI
	public Collection<IRI> areasWorkedIn;

	@Predicate("colleague")
	@ObjectIRI
	public Collection<IRI> colleagues;

	@Predicate("familyName")
	@ObjectString
	public String familyName;

	@Predicate("gender")
	@ObjectString
	public String gender;

	@Predicate("givenName")
	@ObjectString
	public String givenName;

	@Predicate("honorificPrefix")
	@ObjectString
	public String honorificPrefix;

	@Predicate("honorificSuffix")
	@ObjectString
	public String honorificSuffix;

	@Predicate("jobTitle")
	@ObjectString
	public Collection<String> jobTitle;

	/*
	 * The most generic bi-directional social/work relation.
	 */
	@Predicate("knows")
	@ObjectIRI
	public Collection<IRI> knows;

	@Predicate("memberOf")
	@ObjectIRI
	public Collection<IRI> memberOf;// Organization

	@Predicate("nationality")
	@ObjectIRI
	public IRI nationality;

	@Predicate("parents")
	@ObjectIRI
	public Collection<IRI> parents;

	@Predicate("relatedTo")
	@ObjectIRI
	public Collection<IRI> relatedTo;

	@Predicate("sibling")
	@ObjectIRI
	public Collection<IRI> sibling;

	@Predicate("workedOn")
	@SchemaComicWiki
	@ObjectIRI
	//creativeWork,story,character,organization
	public Collection<IRI> workedOn;

	@Predicate("worksFor")
	@ObjectIRI
	public Collection<IRI> worksFor;

	public Person() {
	}

	public void addAreasWorkedIn(IRI workedIn) {
		if (areasWorkedIn   == null) {
			areasWorkedIn   = new HashSet<>(3);
		}
		areasWorkedIn.add(workedIn);
	}
	
	public void addWorkedOn(IRI worked) {
		if (workedOn  == null) {
			workedOn  = new HashSet<>(3);
		}
		workedOn.add(worked);
	}
	
	public void addWorksFor(IRI works) {
		if (worksFor == null) {
			worksFor = new HashSet<>(3);
		}
		worksFor.add(works);
	}
	
	public void addColleague(IRI colleague) {
		if (colleagues == null) {
			colleagues = new HashSet<>(3);
		}
		colleagues.add(colleague);
	}

	public void addJobTitle(String title) {
		if (jobTitle == null) {
			jobTitle = new HashSet<>(3);
		}
		jobTitle.add(title);
	}

	public void addKnows(IRI know) {
		if (knows == null) {
			knows = new HashSet<>(3);
		}
		knows.add(know);
	}

	public void addMemberOf(IRI m) {
		if (memberOf == null) {
			memberOf = new HashSet<>(3);
		}
		memberOf.add(m);
	}

	public void addParent(IRI parent) {
		if (parents == null) {
			parents = new HashSet<>(2);
		}
		parents.add(parent);
	}

	public void addRelatedTo(IRI related) {
		if (relatedTo == null) {
			relatedTo = new HashSet<>(2);
		}
		relatedTo.add(related);
	}

	public void addSibling(IRI s) {
		if (sibling == null) {
			sibling = new HashSet<>(2);
		}
		sibling.add(s);
	}

	public void makeFemale() {
		this.gender = "F";
	}

	public void makeMale() {
		this.gender = "M";
	}

}
