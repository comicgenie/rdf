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
import org.comicwiki.rdf.annotations.ObjectNumber;
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
	public Collection<IRI> artists  = new HashSet<>(3);
	
	@Predicate("characterNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> characterNote = new HashSet<>(3);
	
	@Predicate("storyNote")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> storyNote = new HashSet<>(3);
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<IRI> colorists  = new HashSet<>(3);
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<IRI> inkers  = new HashSet<>(3);
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<IRI> letterers  = new HashSet<>(3);
	
	@Predicate("organization")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> organizations = new HashSet<>(1);
	
	@Predicate("pageCountUncertain")
	@ObjectBoolean
	@SchemaComicWiki
	public Boolean pageCountUncertain;
	
	@Predicate("pageEnd")
	@ObjectNumber
	@SchemaComicWiki
	public int pageEnd;
	
	@Predicate("pageStart")
	@ObjectNumber
	@SchemaComicWiki
	public int pageStart;
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<IRI> pencilers  = new HashSet<>(3);
	
	@Predicate("storyType")
	@ObjectString
	@SchemaComicWiki
	public String storyType;
	
	@Predicate("jobCode")
	@ObjectString
	@SchemaComicWiki
	public String jobCode;
	
	@Predicate("pageCount")
	@ObjectNumber
	@SchemaComicWiki
	public int pageCount;
	
}
