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
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/Organization", key="name")
public class Organization extends Thing {

	@Predicate("brand")
	@ObjectIRI
	public IRI[] brands;
	
	@Predicate("dissolutionDate")
	@ObjectIRI
	public IRI dissolutionDate;
	
	@Predicate("founder")
	public IRI[] founders;
	
	@Predicate("foundingDate")
	@ObjectIRI
	public IRI foundingDate;
	
	@Predicate("legalName")
	@ObjectString
	public String legalName;
	
	@Predicate("location")//should this be collection???
	@ObjectIRI
	public IRI location;//Place
	
	/**
	 * ID of person (name:universe:era)
	 */
	@Predicate("member")
	@ObjectIRI
	public IRI[] members;
	
	@Predicate("parentOrganization")
	@ObjectIRI
	public IRI parentOrganization;
	
	@Predicate("subOrganization")
	@ObjectIRI
	public IRI[] subOrganization;
	
	public void addMembers(IRI member) {
		members = Add.one(members, member);
	}
	
	public void addSubOrganization(IRI org) {
		subOrganization = Add.one(subOrganization, org);
	}
	
	public void addFounder(IRI founder) {
		founders = Add.one(founders, founder);
	}
	
	public void addBrand(IRI brand) {
		brands= Add.one(brands, brand);
	}
}
