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

import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectInteger;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/ComicStory", key= "name")
public class ComicStory extends CreativeWork {
	
	@Predicate("artist")
	@ObjectIRI
	public Collection<String> artists  = new HashSet<>(3);
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<String> colorists  = new HashSet<>(3);
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<String> letterers  = new HashSet<>(3);
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<String> pencilers  = new HashSet<>(3);
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<String> inkers  = new HashSet<>(3);
	
	//Add following [not in schema.org]
	@Predicate("pageEnd")
	@ObjectInteger
	public int pageEnd;
	
	@Predicate("pageStart")
	@ObjectInteger
	public int pageStart;
	
	//[not in schema.org]
	@Predicate("organizations")
	@ObjectIRI
	public Collection<String> organizations = new HashSet<>();
	
	@Predicate("pageCountUncertain")
	@ObjectBoolean
	public boolean pageCountUncertain;
	
}