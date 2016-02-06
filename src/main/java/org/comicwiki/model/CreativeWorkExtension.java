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
package org.comicwiki.model;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
@Subject(value = "Extension", key= "publisher")
public class CreativeWorkExtension  {

	@Predicate("comment")
	@ObjectString
	public Collection<String> comment = new HashSet<>(2);
	
	//For Creative Work [ComicStory] Inheritence
	@Predicate("genre")
	@ObjectString
	public Collection<String> genres = new HashSet<>(3);

	/**
	 * Indicates a CreativeWork that this CreativeWork is (in some sense) part
	 * of.
	 */
	@Predicate("isPartOf")
	@ObjectIRI
	public Collection<String> isPartOf = new HashSet<>();// CreativeWork
	
	@Predicate("hasPart")
	@ObjectIRI
	public Collection<String> hasParts = new HashSet<>();// CreativeWork - PublicationVolume
	
	@Predicate("author")
	@ObjectIRI
	public Collection<String> authors = new HashSet<>(3);

	@Predicate("publisher")
	@ObjectIRI
	public String publisher;

	@Predicate("editor")
	@ObjectIRI
	public Collection<String> editors = new HashSet<>(3);
	
	@Predicate("artist")
	@ObjectIRI
	public Collection<String> artists  = new HashSet<>(3);
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<String> colorists  = new HashSet<>(3);
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<String> letterers  = new HashSet<>(3);
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<String> pencilers  = new HashSet<>(3);
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<String> inkers  = new HashSet<>(3);
	
	
}
