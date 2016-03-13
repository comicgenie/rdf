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
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/CreativeWork", key = "name")
public class CreativeWork extends Thing {

	@Predicate("author")
	@ObjectIRI
	public Collection<IRI> authors;
	
	@Predicate("character")
	@ObjectIRI
	public Collection<IRI> characters;
	
	/**
	 * A citation or reference to another creative work, such as another
	 * publication, web page, scholarly article, etc.
	 */
	@Predicate("citation")
	@ObjectIRI
	public String citation;
	
	@Predicate("comment")
	@ObjectString
	public Collection<String> comment;
		
	/**
	 * Official rating of a piece of content—for example,'MPAA PG-13'.
	 */
	@Predicate("contentRating")
	@ObjectString
	public String contentRating;// this will map to issue.rating
	
	@Predicate("creatorAlias")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> creatorAlias;
	
	@Predicate("datePublished")
	@ObjectIRI
	public IRI datePublished;
	
	@Predicate("editor")
	@ObjectIRI
	public Collection<IRI> editors;
	
	@Predicate("exampleOfWork")
	@ObjectIRI
	public Collection<IRI> exampleOfWork;
	
	@Predicate("fictionalOrganizations")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> fictionalOrganizations;
	
	@Predicate("genre")
	@ObjectIRI
	public Collection<IRI> genres;
	
	@Predicate("hasPart")
	@ObjectIRI
	public Collection<IRI> hasParts;// CreativeWork - PublicationVolume

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
	public Collection<IRI> isPartOf;// CreativeWork

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
	public Collection<IRI> publisherImprints;

	@Predicate("publisher")
	@ObjectIRI
	public Collection<IRI> publishers;

	@Predicate("reprint")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprint;

	@Predicate("reprintNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprintNote;
	
	@Predicate("reprintOf")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> reprintOf;
	
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
		if(publisherImprints == null) {
			publisherImprints = new HashSet<>(2);
		}
		publisherImprints.add(publisher);
	}
	
	public void addIsPartOf(IRI part) {
		if(isPartOf == null) {
			isPartOf = new HashSet<>(2);
		}
		isPartOf.add(part);
	}
	
	public void addGenre(IRI genre) {
		if(genres == null) {
			genres = new HashSet<>(3);
		}
		genres.add(genre);
	}
	
	public void addGenre(Collection<IRI> genre) {
		if(genre == null) {
			return;
		}
		if(genres == null) {
			genres = new HashSet<>(3);
		}
		genres.addAll(genre);
	}
	
	public void addFictionalOrganization(IRI org) {
		if(fictionalOrganizations == null) {
			fictionalOrganizations = new HashSet<>(3);
		}
		fictionalOrganizations.add(org);
	}
	
	public void addExampleOfWork(IRI example) {
		if(exampleOfWork == null) {
			exampleOfWork = new HashSet<>(3);
		}
		exampleOfWork.add(example);
	}
	
	public void addComment(String c) {
		if(comment == null) {
			comment = new HashSet<>(3);
		}
		comment.add(c);
	}
	
	public void addAuthor(IRI author) {
		if(authors == null) {
			authors = new HashSet<>(3);
		}
		authors.add(author);
	}

	public void addCharacter(IRI character) {
		if(characters == null) {
			characters = new HashSet<>(5);
		}
		characters.add(character);
	}

	public void addCreatorAlias(IRI alias) {
		if(creatorAlias == null) {
			creatorAlias = new HashSet<>(1); 
		}
		creatorAlias.add(alias);
	}

	public void addEditor(IRI editor) {
		if(editors== null) {
			editors = new HashSet<>(3); 
		}
		editors.add(editor);
	}

	public void addHasPart(IRI part) {
		if(hasParts== null) {
			hasParts = new HashSet<>(3); 
		}
		hasParts.add(part);
	}
	
	public void addPublisher(IRI publisher) {
		if(publishers== null) {
			publishers = new HashSet<>(1);
		}
		publishers.add(publisher);
	}
		
	public void addReprint(IRI r) {
		if(reprint == null) {
			reprint = new HashSet<>(1);
		}
		reprint.add(r);
	}
	
	public void addReprintNote(IRI note) {
		if(reprintNote == null) {
			reprintNote = new HashSet<>(1);
		}
		reprintNote.add(note);
	}
	
	public void addReprintOf(IRI reprint) {
		if(reprintOf == null) {
			reprintOf = new HashSet<>(1);
		}
		reprintOf.add(reprint);
	}
		
}
