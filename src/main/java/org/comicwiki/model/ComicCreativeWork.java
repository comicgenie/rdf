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

import org.comicwiki.Add;
import org.comicwiki.IRI;
import org.comicwiki.model.schema.CreativeWork;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;

@SchemaComicWiki
public class ComicCreativeWork extends CreativeWork {

	@Predicate("artist")
	@ObjectIRI
	public IRI[] artists;
	
	@Predicate("colorist")
	@ObjectIRI
	public IRI[] colorists;
	
	@Predicate("editor")
	@ObjectIRI
	public IRI[] editors;
	
	@Predicate("inker")
	@ObjectIRI
	public IRI[] inkers;
	
	@Predicate("letterer")
	@ObjectIRI
	public IRI[] letterers;
	
	@Predicate("penciler")
	@ObjectIRI
	public IRI[] pencilers;
	
	public void addEditor(IRI editor) {
		editors = Add.one(editors, editor);
	}
	
	public void addArtist(IRI artist) {
		artists = Add.one(artists, artist);
	}
	
	public void addColorist(IRI colorist) {	
		colorists = Add.one(colorists, colorist);
	}
	
	public void addCreatorAlias(IRI alias) {
		creatorAlias = Add.one(creatorAlias, alias);
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
	
}
