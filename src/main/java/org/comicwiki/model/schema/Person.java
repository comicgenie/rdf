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

import org.comicwiki.Add;
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
	public IRI[] areasWorkedIn;

	@Predicate("colleague")
	@ObjectIRI
	public IRI[] colleagues;

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
	public String[] jobTitle;

	/*
	 * The most generic bi-directional social/work relation.
	 */
	@Predicate("knows")
	@ObjectIRI
	public IRI[] knows;

	@Predicate("memberOf")
	@ObjectIRI
	public IRI[] memberOf;// Organization

	@Predicate("nationality")
	@ObjectIRI
	public IRI nationality;

	@Predicate("parents")
	@ObjectIRI
	public IRI[] parents;

	@Predicate("relatedTo")
	@ObjectIRI
	public IRI[] relatedTo;

	@Predicate("sibling")
	@ObjectIRI
	public IRI[] sibling;

	@Predicate("workedOn")
	@SchemaComicWiki
	@ObjectIRI
	//creativeWork,story,character,organization
	public IRI[] workedOn;

	@Predicate("worksFor")
	@ObjectIRI
	public IRI[] worksFor;

	public Person() {
	}

	public void addAreasWorkedIn(IRI workedIn) {
		areasWorkedIn = Add.one(areasWorkedIn, workedIn);	
	}
	
	public void addWorkedOn(IRI worked) {
		workedOn = Add.one(workedOn, worked);
	}
	
	public void addWorksFor(IRI works) {
		worksFor = Add.one(worksFor, works);
	}
	
	public void addColleague(IRI colleague) {
		colleagues = Add.one(colleagues, colleague);
	}

	public void addJobTitle(String title) {
		jobTitle = Add.one(jobTitle, title);
	}

	public void addKnows(IRI know) {
		knows = Add.one(knows, know);
	}

	public void addMemberOf(IRI m) {
		memberOf = Add.one(memberOf, m);
	}

	public void addParent(IRI parent) {
		parents = Add.one(parents, parent);
	}

	public void addRelatedTo(IRI related) {
		relatedTo = Add.one(relatedTo, related);
	}

	public void addSibling(IRI s) {
		sibling = Add.one(sibling, s);
	}
	
	public void makeFemale() {
		this.gender = "F";
	}

	public void makeMale() {
		this.gender = "M";
	}

}
