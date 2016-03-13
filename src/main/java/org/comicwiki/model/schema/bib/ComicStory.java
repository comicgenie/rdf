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
import org.comicwiki.model.schema.CreativeWork;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/ComicStory", key= "name")
@SchemaBib
public class ComicStory extends CreativeWork {
	
	@Predicate("artist")
	@ObjectIRI
	public Collection<IRI> artists;
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<IRI> colorists;
	
	@Predicate("creatorAlias")
	@ObjectIRI
	public Collection<IRI> creatorAlias;
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<IRI> inkers;
	
	@Predicate("jobCode")
	@ObjectString
	@SchemaComicWiki
	public String jobCode;
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<IRI> letterers;
	
	@Predicate("organization")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> organizations;
	
	@Predicate("pageCount")
	@ObjectNonNegativeInteger
	@SchemaComicWiki
	public int pageCount;
	
	@Predicate("pageCountUncertain")
	@ObjectBoolean
	@SchemaComicWiki
	public Boolean pageCountUncertain;
	
	@Predicate("pageEnd")
	@ObjectNonNegativeInteger
	@SchemaComicWiki
	public int pageEnd;
	
	@Predicate("pageStart")
	@ObjectNonNegativeInteger
	@SchemaComicWiki
	public int pageStart;
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<IRI> pencilers;
	
	@Predicate("storyNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> storyNote;
	
	@Predicate("storyType")
	@ObjectString
	@SchemaComicWiki
	public String storyType;
	
	public void addArtist(IRI artist) {
		if(artists == null) {
			artists = new HashSet<>(3); 
		}
		artists.add(artist);
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
	
	public void addLetter(IRI letterer) {
		if(letterers== null) {
			letterers = new HashSet<>(3); 
		}
		letterers.add(letterer);
	}
	
	public void addOrganization(IRI org) {
		if(organizations == null) {
			organizations = new HashSet<>(1); 
		}
		organizations.add(org);
	}
	
	public void addPenciler(IRI penciler) {
		if(pencilers== null) {
			pencilers = new HashSet<>(3); 
		}
		pencilers.add(penciler);
	}
	
	public void addStoryNote(IRI note) {
		if(storyNote == null) {
			storyNote = new HashSet<>(3); 
		}
		storyNote.add(note);
	}
	
}
