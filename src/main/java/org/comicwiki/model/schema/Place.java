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

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;
/**
 * Entities that have a somewhat fixed, physical extension.
 */
@Subject(value = "http://schema.org/Place", key= "name")
public class Place extends Thing {
	
	/**
	 * The basic containment relation between a place and one that contains it.
	 */
	@Predicate("containedInPlace")
	@ObjectIRI
	public Collection<IRI> containedInPlaces = new HashSet<>(5);
	
	/**
	 * The basic containment relation between a place and another that it contains.
	 */
	@Predicate("containsPlace")
	@ObjectIRI
	public Collection<IRI> containsPlaces = new HashSet<>(5);
}
