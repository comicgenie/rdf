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

import org.comicwiki.model.schema.Organization;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "ComicOrganization", key = "name")
//TODO: probably need to add universe/publisher
public class ComicOrganization extends Organization /*[CreativeWork]*/{

	@Predicate("alignment")
	@ObjectString
	public Alignment alignment;

	@Predicate("organizationType")
	@ObjectString
	public OrganizationType organizationType;

	@Predicate("ComicUniverse")
	@ObjectIRI
	public Collection<ComicUniverse> universes = new HashSet<>(1);
	
	//TODO: Need a special ser/der for this
	public CreativeWorkExtension creativeWork = new CreativeWorkExtension();

}
