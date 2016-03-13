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

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/PublicationIssue", compositeKey= {"name", "issueNumber"})
public class PublicationIssue extends CreativeWork {

	@Predicate("dateOnSale")
	@ObjectIRI
	@SchemaComicWiki
	public IRI dateOnSale;
	
	/**
	 * A PublicationIssue isPartOf PublicationVolume [ how to use in ID]
	 */
	

	@Predicate("issueNumber")
	@ObjectString
	public String issueNumber;
	
	@Predicate("pageEnd")
	@ObjectNonNegativeInteger
	public int pageEnd;
	
	@Predicate("pageStart")
	@ObjectNonNegativeInteger
	public int pageStart;
	
	@Predicate("pagination")
	@ObjectString
	public String pagination;
	

}
