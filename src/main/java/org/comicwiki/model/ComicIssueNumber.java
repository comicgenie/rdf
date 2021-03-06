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

import static org.comicwiki.rdf.DataType.XSD_GYEAR;

import org.comicwiki.Add;
import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ComicIssueNumber", isBlankNode = true)
public class ComicIssueNumber extends Intangible {

	/**
	 * Assigned by indexer
	 */
	@Predicate("assigned")
	@ObjectNonNegativeInteger
	public Integer assigned;
	
	@Predicate("cover")
	@ObjectNonNegativeInteger
	public Integer cover;
	
	@Predicate("isNonStandardGCDFormat")
	@ObjectBoolean
	public Boolean isNonStandardGCDFormat;
	
	@Predicate("issueNumber")
	@ObjectNonNegativeInteger
	public Integer[] issueNumbers;
	
	@Predicate("label")
	@ObjectIRI
	public String label;
	
	@Predicate("year")
	@ObjectXSD(XSD_GYEAR)
	public Integer year;
	
	public void addIssueNumber(Integer number) {
		issueNumbers = Add.one(issueNumbers, number);
	}
	
	
}
