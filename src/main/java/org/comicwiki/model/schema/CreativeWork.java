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

import org.comicwiki.Add;
import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/CreativeWork", key = "name")
public class CreativeWork extends Thing {

	@Predicate("author")
	@ObjectIRI
	public IRI[] authors;
	
	@Predicate("character")
	@ObjectIRI
	public IRI[] characters;
	
	/**
	 * A citation or reference to another creative work, such as another
	 * publication, web page, scholarly article, etc.
	 */
	@Predicate("citation")
	@ObjectIRI
	public String citation;
	
	@Predicate("comment")
	@ObjectString
	public String[] comment;
		
	/**
	 * Official rating of a piece of content—for example,'MPAA PG-13'.
	 */
	@Predicate("contentRating")
	@ObjectString
	public String contentRating;// this will map to issue.rating
	
	@Predicate("creatorAlias")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] creatorAlias;
	
	@Predicate("datePublished")
	@ObjectIRI
	public IRI datePublished;
	
	@Predicate("editor")
	@ObjectIRI
	public IRI[] editors;
	
	@Predicate("exampleOfWork")
	@ObjectIRI
	public IRI[] exampleOfWork;
	
	@Predicate("fictionalOrganizations")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] fictionalOrganizations;
	
	@Predicate("genre")
	@ObjectIRI
	public IRI[] genres;
	
	@Predicate("hasPart")
	@ObjectIRI
	public IRI[] hasParts;// CreativeWork - PublicationVolume

	@Predicate("headline")
	@ObjectString
	public String headline;// featured for comic story

	/**
	 * The language of the content or performance or used in an action. Please
	 * use one of the language codes from the IETF BCP 47 standard.
	 */
	@Predicate("inLanguage")
	@ObjectIRI
	public IRI inLanguage;

	/**
	 * Indicates whether this content is family friendly.
	 */
	@Predicate("isFamilyFriendly")
	@ObjectBoolean
	public Boolean isFamilyFriendly;

	/**
	 * Indicates a CreativeWork that this CreativeWork is (in some sense) part
	 * of.
	 */
	@Predicate("isPartOf")
	@ObjectIRI
	public IRI[] isPartOf;// CreativeWork

	@Predicate("locationCreated")
	@ObjectIRI
	public IRI locationCreated;// place

	/*
	 * The position of an item in a series or sequence of items.
	 */
	@Predicate("position")
	@ObjectNonNegativeInteger
	public int position;

	/*
	 * The publishing division which published the comic.
	 * http://bib.schema.org/publisherImprint
	 */
	@Predicate("publisherImprints")
	@ObjectIRI
	@SchemaBib
	public IRI[] publisherImprints;

	@Predicate("publisher")
	@ObjectIRI
	public IRI[] publishers;

	@Predicate("reprint")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] reprint;

	@Predicate("reprintNote")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] reprintNote;
	
	@Predicate("reprintOf")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] reprintOf;
	
	/**
	 * The textual content of this CreativeWork.
	 */
	@Predicate("contentRating")
	@ObjectString
	public String text;

	/**
	 * The typical expected age range, e.g. '7-9', '11-'.
	 */
	@Predicate("typicalAgeRange")
	@ObjectString
	public String typicalAgeRange;
	
	public void addPublisherImprints(IRI publisher) {
		publisherImprints = Add.one(publisherImprints, publisher);
	}
	
	public void addIsPartOf(IRI part) {
		isPartOf = Add.one(isPartOf, part);
	}
	
	public void addGenre(IRI genre) {
		genres = Add.one(genres, genre);
	}
	
	public void addGenre(Collection<IRI> genre) {
		genres = Add.both(genres, genre, IRI.class);
	}
	
	public void addGenre(IRI[] genre) {
		genres = Add.both(genres, genre, IRI.class);
	}
	
	public void addFictionalOrganization(IRI org) {
		fictionalOrganizations = Add.one(fictionalOrganizations, org);
	}
	
	public void addExampleOfWork(IRI example) {
		exampleOfWork = Add.one(exampleOfWork, example);
	}
	
	public void addComment(String c) {
		comment = Add.one(comment, c);
	}
	
	public void addAuthor(IRI author) {
		authors = Add.one(authors, author);	
	}

	public void addCharacter(IRI character) {
		characters = Add.one(characters, character);
	}

	public void addCreatorAlias(IRI alias) {
		creatorAlias = Add.one(creatorAlias, alias);
	}

	public void addEditor(IRI editor) {
		editors = Add.one(editors, editor);
	}

	public void addHasPart(IRI part) {
		hasParts = Add.one(hasParts, part);
	}
	
	public void addPublisher(IRI publisher) {
		publishers = Add.one(publishers, publisher);
	}
		
	public void addReprint(IRI r) {
		reprint = Add.one(reprint, r);
	}
	
	public void addReprintNote(IRI note) {
		reprintNote = Add.one(reprintNote, note);
	}
	
	public void addReprintOf(IRI reprint) {
		reprintOf = Add.one(reprintOf, reprint);
	}
		
}
