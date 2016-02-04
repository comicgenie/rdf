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

import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/ComicIssue",  compositeKey= {"name", "issueNumber"})
public class ComicIssue extends PublicationIssue {

	//ID: title:vol:num
	
	@Predicate("artist")
	@ObjectIRI
	public Collection<String> artists  = new HashSet<>(1);//person
	
	@Predicate("colorist")
	@ObjectIRI
	public Collection<String> colorists  = new HashSet<>(1);
	
	@Predicate("letterer")
	@ObjectIRI
	public Collection<String> letterers  = new HashSet<>(1);
	
	@Predicate("penciler")
	@ObjectIRI
	public Collection<String> pencilers  = new HashSet<>(1);
	
	@Predicate("inker")
	@ObjectIRI
	public Collection<String> inkers  = new HashSet<>(1);
	
	//Add this [not in spec]
	@Predicate("brand")
	@ObjectIRI
	public Collection<String> brands  = new HashSet<>(1);//Brand
	
}
