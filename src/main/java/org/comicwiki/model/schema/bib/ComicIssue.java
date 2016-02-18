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
	public Collection<IRI> artists  = new HashSet<>(1);//person
	
	@Predicate("colorist")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> colorists  = new HashSet<>(1);
	
	@Predicate("letterer")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> letterers  = new HashSet<>(1);
	
	@Predicate("penciler")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> pencilers  = new HashSet<>(1);
	
	@Predicate("inker")
	@ObjectIRI
	@SchemaBib
	public Collection<IRI> inkers  = new HashSet<>(1);
	
	@Predicate("brand")
	@ObjectIRI
	@SchemaComicWiki
	public Collection<IRI> brands  = new HashSet<>(1);//Brand
	
}
