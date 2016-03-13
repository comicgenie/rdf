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

import org.comicwiki.IRI;
import org.comicwiki.model.schema.CreativeWork;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;

@SchemaComicWiki
public class ComicCreativeWork extends CreativeWork {

	@Predicate("artist")
	@ObjectIRI
	public Collection<IRI> artists;
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<IRI> colorists;
	
	@Predicate("editor")
	@ObjectIRI
	public Collection<IRI> editors;
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<IRI> inkers;
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<IRI> letterers;
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<IRI> pencilers;
	
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
	
	public void addEditor(IRI editor) {
		if(editors== null) {
			editors = new HashSet<>(3); 
		}
		editors.add(editor);
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
	
	public void addPenciler(IRI penciler) {
		if(pencilers== null) {
			pencilers = new HashSet<>(3); 
		}
		pencilers.add(penciler);
	}
	
	
}
