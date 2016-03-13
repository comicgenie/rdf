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
package org.comicwiki.model.schema.bib;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.IRI;
import org.comicwiki.model.schema.PublicationIssue;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/ComicIssue",  compositeKey= {"name", "issueNumber"})
@SchemaBib
public class ComicIssue extends PublicationIssue {

	//ID: title:vol:num
	
	@Predicate("artist")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> artists;
	
	@Predicate("brand")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> brands;
	
	@Predicate("colorist")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> colorists;
	
	@Predicate("frequency")
	@ObjectString
	@SchemaComicWiki
	public String frequency;
	
	@Predicate("inker")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> inkers;
	
	@Predicate("issueNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> issueNote;
	
	@Predicate("issueNumber")
	@ObjectIRI
	@SchemaComicWiki
	public IRI issueNumber;
	
	@Predicate("letterer")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> letterers;
	
	@Predicate("penciler")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> pencilers;
	
	@Predicate("price")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> price;
	
	public void addArtist(IRI artist) {
		if(artists == null) {
			artists = new HashSet<>(3); 
		}
		artists.add(artist);
	}
	
	public void addBrand(IRI brand) {
		if(brands == null) {
			brands = new HashSet<>(3); 
		}
		brands.add(brand);
	}
	
	public void addColorist(IRI colorist) {
		if(colorists == null) {
			colorists = new HashSet<>(3); 
		}
		colorists.add(colorist);
	}
	
	public void addCreatorAlias(IRI alias) {
		if(creatorAlias == null) {
			creatorAlias = new HashSet<>(1); 
		}
		creatorAlias.add(alias);
	}
	
	public void addInker(IRI inker) {
		if(inkers == null) {
			inkers = new HashSet<>(3); 
		}
		inkers.add(inker);
	}
	
	public void addIssueNote(IRI note) {
		if (issueNote == null) {
			issueNote = new HashSet<>(3); 
		}
		issueNote.add(note);
	}
	
	public void addLetter(IRI letterer) {
		if(letterers== null) {
			letterers = new HashSet<>(3); 
		}
		letterers.add(letterer);
	}
	
	public void addPenciler(IRI penciler) {
		if(pencilers== null) {
			pencilers = new HashSet<>(3); 
		}
		pencilers.add(penciler);
	}
	
	public void addPrice(IRI p) {
		if(price == null) {
			price = new HashSet<>(3); 
		}
		price.add(p);
	}
}
