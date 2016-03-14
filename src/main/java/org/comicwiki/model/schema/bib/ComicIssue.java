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

import org.comicwiki.Add;
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
	public IRI[] artists;
	
	@Predicate("brand")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] brands;
	
	@Predicate("colorist")
	@ObjectIRI
	@SchemaBib
	public IRI[] colorists;
	
	@Predicate("frequency")
	@ObjectString
	@SchemaComicWiki
	public String frequency;
	
	@Predicate("inker")
	@ObjectIRI
	@SchemaBib
	public IRI[] inkers;
	
	@Predicate("issueNote")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] issueNote;
	
	@Predicate("issueNumber")
	@ObjectIRI
	@SchemaComicWiki
	public IRI issueNumber;
	
	@Predicate("letterer")
	@ObjectIRI
	@SchemaBib
	public IRI[] letterers;
	
	@Predicate("penciler")
	@ObjectIRI
	@SchemaBib
	public IRI[] pencilers;
	
	@Predicate("price")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] price;
	
	public void addArtist(IRI artist) {
		artists = Add.one(artists, artist);
	}
	
	public void addBrand(IRI brand) {
		brands = Add.one(brands, brand);
	}

	public void addColorist(IRI colorist) {	
		colorists = Add.one(colorists, colorist);
	}
	
	public void addInker(IRI inker) {
		inkers = Add.one(inkers, inker);
	}
	
	public void addLetter(IRI letterer) {
		letterers = Add.one(letterers, letterer);
	}
	
	public void addPenciler(IRI penciler) {
		pencilers = Add.one(pencilers, penciler);
	}
	
	public void addIssueNote(IRI note) {
		issueNote = Add.one(issueNote, note);
	}
	
	public void addPrice(IRI p) {
		price = Add.one(price, p);
	}
}
