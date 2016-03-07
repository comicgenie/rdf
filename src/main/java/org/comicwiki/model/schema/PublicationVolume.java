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

import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.Subject;

/**
 * A part of a successively published publication such as a periodical or
 * multi-volume work, often numbered. It may represent a time span, such as a
 * year.
 */
@Subject(value = "http://bib.schema.org/PublicationVolume",  compositeKey= {"name", "volumeNumber"})
@SchemaBib
public class PublicationVolume extends CreativeWork {

	/**
	 * The page on which the work ends; for example "138" or "xvi".
	 */
	@Predicate("pageEnd")
	@ObjectNonNegativeInteger
	public int pageEnd;

	/**
	 * The page on which the work starts; for example "135" or "xiii".
	 */
	@Predicate("pageStart")
	@ObjectNonNegativeInteger
	public int pageStart;

	/**
	 * Any description of pages that is not separated into pageStart and
	 * pageEnd; for example, "1-6, 9, 55" or "10-12, 46-49".
	 */
	@Predicate("pagination")
	@ObjectString
	public String pagination;

	/**
	 * Identifies the volume of publication or multi-part work; for example,
	 * "iii" or "2".
	 */
	@Predicate("volumeNumber")
	@ObjectString
	public String volumeNumber;

}
