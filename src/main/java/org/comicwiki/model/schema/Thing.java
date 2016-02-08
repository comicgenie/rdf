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

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ObjectURL;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
@Subject(value = "http://schema.org/Thing", key= "name")
public class Thing {

	public final static String CONTEXT = "http://schema.org/";

	@Predicate(CONTEXT + "url")
	@ObjectURL
	public final Collection<URI> urls = new HashSet<>(5);

	@Predicate(CONTEXT + "name")
	@ObjectString
	public String name;

	@Predicate(CONTEXT + "description")
	@ObjectString
	public Collection<String> description = new HashSet<>();

	@Predicate(CONTEXT + "alternateName")
	@ObjectString
	public final Collection<String> alternateNames = new HashSet<>(5);
	
	//[not in spec]
	@Predicate( "http://comicwiki.org/resources/resourceId")
	@ObjectString
	public String resourceId;
	
	public String internalId;

	@Override
	public String toString() {
		return "Thing [url=" + urls + ", name=" + name + ", description="
				+ description + ", alternateNames=" + alternateNames + "]";
	}

}
