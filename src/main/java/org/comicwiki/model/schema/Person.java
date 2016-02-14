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

import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/Person", key = "name")
public class Person extends Thing {

	public Person() { }
	
	@Predicate("honorificPrefix")
	@ObjectString
	public String honorificPrefix;
	
	@Predicate("honorificSuffix")
	@ObjectString
	public String honorificSuffix;
	
	@Predicate("givenName")
	@ObjectString
	public String givenName;
	
	@Predicate("familyName")
	@ObjectString
	public String familyName;
	
	@Predicate("colleague")
	@ObjectIRI
	public Collection<String> colleagues = new HashSet<>();

	@Predicate("gender")
	@ObjectString
	public String gender;

	@Predicate("jobTitle")
	@ObjectString
	public Collection<String> jobTitle = new HashSet<>(2);

	/*
	 * The most generic bi-directional social/work relation.
	 */
	@Predicate("knows")
	@ObjectIRI
	public Collection<String> knows = new HashSet<>();

	@Predicate("memberOf")
	@ObjectIRI
	public Collection<String> memberOf = new HashSet<>(5);// Organization

	@Predicate("nationality")
	@ObjectIRI
	public Country nationality;

	@Predicate("parents")
	@ObjectIRI
	public Collection<String> parents = new HashSet<>(2);

	@Predicate("relatedTo")
	@ObjectIRI
	public Collection<String> relatedTo = new HashSet<>(3);

	@Predicate("sibling")
	@ObjectIRI
	public Collection<String> sibling = new HashSet<>(3);

	@Predicate("worksFor")
	@ObjectIRI
	public Collection<String> worksFor = new HashSet<>(3);// Organization

	// [non-standard]
	@Predicate("areasWorkedIn")
	@ObjectIRI
	public Collection<String> areasWorkedIn = new HashSet<>(3);// genres

	@Predicate("workedOn")
	@ObjectIRI
	public Collection<String> workedOn = new HashSet<>(3);// creativeWork,
																// story,
																// character,															// organization
	public void makeMale() {
		this.gender = "M";
	}
	
	public void makeFemale() {
		this.gender = "F";
	}
	

	@Override
	public String toString() {
		return "Person [gender=" + gender + ", knows=" + knows + ", memberOf="
				+ memberOf + ", nationality=" + nationality + ", parents="
				+ parents + ", relatedTo=" + relatedTo + ", sibling=" + sibling
				+ ", worksFor=" + worksFor + ", url=" + urls + ", name=" + name
				+ ", description=" + description + ", alternateNames="
				+ alternateNames + "]";
	}

}
