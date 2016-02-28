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
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/CreativeWork", key = "name")
public class CreativeWork extends Thing {

	@Predicate("comment")
	@ObjectString
	public Collection<String> comment = new HashSet<>(2);
	
	/*
	 * The position of an item in a series or sequence of items.
	 */
	@Predicate("position")
	@ObjectNumber
	public int position;

	@Predicate("headline")
	@ObjectString
	public String headline;// featured for comic story

	@Predicate("genre")
	@ObjectIRI
	public Collection<IRI> genres = new HashSet<>(3);

	@Predicate("hasPart")
	@ObjectIRI
	public Collection<String> hasParts = new HashSet<>();// CreativeWork - PublicationVolume

	/**
	 * Indicates a CreativeWork that this CreativeWork is (in some sense) part
	 * of.
	 */
	@Predicate("isPartOf")
	@ObjectIRI
	public Collection<String> isPartOf = new HashSet<>();// CreativeWork

	@Predicate("author")
	@ObjectIRI
	public Collection<IRI> authors = new HashSet<>(3);

	@Predicate("publisher")
	@ObjectIRI
	public Collection<IRI> publishers = new HashSet<>(1);// organization

	@Predicate("character")
	@ObjectIRI
	public Collection<IRI> characters = new HashSet<>();

	@Predicate("editor")
	@ObjectIRI
	public Collection<IRI> editors = new HashSet<>(3);

	@Predicate("exampleOfWork")
	@ObjectIRI
	public Collection<IRI> exampleOfWork = new HashSet<>();// CreativeWork

	@Predicate("locationCreated")
	@ObjectIRI
	public String locationCreated;// place
	
	/*
	 * The publishing division which published the comic.
	 * http://bib.schema.org/publisherImprint
	 */
	@Predicate("publisherImprints")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> publisherImprints = new HashSet<>(2);//Organization
	
	/**
	 * Indicates whether this content is family friendly.
	 */
	@Predicate("isFamilyFriendly")
	@ObjectBoolean
	public Boolean isFamilyFriendly;

	/**
	 * The textual content of this CreativeWork.
	 */
	@Predicate("contentRating")
	@ObjectString
	public String text;

	/**
	 * The language of the content or performance or used in an action. Please
	 * use one of the language codes from the IETF BCP 47 standard.
	 */
	@Predicate("inLanguage")
	@ObjectString
	public String inLanguage;

	@Predicate("datePublished")
	@ObjectIRI
	public IRI datePublished;

	/**
	 * The typical expected age range, e.g. '7-9', '11-'.
	 */
	@Predicate("typicalAgeRange")
	@ObjectString
	public String typicalAgeRange;

	/**
	 * A citation or reference to another creative work, such as another
	 * publication, web page, scholarly article, etc.
	 */
	@Predicate("citation")
	@ObjectIRI
	public String citation;

	/**
	 * Official rating of a piece of content—for example,'MPAA PG-13'.
	 */
	@Predicate("contentRating")
	@ObjectString
	public String contentRating;// this will map to issue.rating
	
	@Predicate("creatorAlias")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> creatorAlias = new HashSet<>(1);
		
	@Predicate("reprint")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprint = new HashSet<>(1);
	
	@Predicate("reprintOf")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprintOf = new HashSet<>(1);
	
	@Predicate("reprintNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprintNote = new HashSet<>(1);
		
}
