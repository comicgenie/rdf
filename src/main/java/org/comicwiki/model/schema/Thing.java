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
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ObjectURL;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
@Subject(value = "http://schema.org/Thing", key= "name")
public class Thing {

	public final static String CONTEXT = "http://schema.org/";

	@Predicate(CONTEXT + "alternateName")
	@ObjectString
	public Collection<String> alternateNames;

	@Predicate( "http://comicwiki.org/resources/compositePropertyKey")
	@ObjectString
	@SchemaComicWiki
	public String compositePropertyKey;

	@Predicate(CONTEXT + "description")
	@ObjectString
	public Collection<String> description;

	@SchemaComicWiki
	@ObjectIRI
	public IRI instanceId;
	
	@Predicate(CONTEXT + "name")
	@ObjectString
	public String name;
	
	@Predicate( "http://comicwiki.org/resources/resourceId")
	@ObjectIRI
	@SchemaComicWiki
	public IRI resourceId;
	
	@Predicate(CONTEXT + "url")
	@ObjectURL
	public Collection<URL> urls;

	public void addAlternateName(String alternateName) {
		if(alternateNames == null) {
			alternateNames = new HashSet<>(2);
		}
		alternateNames.add(alternateName);
	}
	
	public void addAlternateName(Collection<String> alternateName) {
		if(alternateName == null) {
			return;
		}
		if(alternateNames == null) {
			alternateNames = new HashSet<>(2);
		}
		alternateNames.addAll(alternateName);
	}
	
	public void addDescription(String descript) {
		if(description == null) {
			description = new HashSet<>(1);
		}
		description.add(descript);
	}
	
	public void addUrl(URL url) {
		if(urls == null) {
			urls = new HashSet<>(2);
		}
		urls.add(url);
	}
	
	@Override
	public String toString() {
		return "Thing [urls=" + urls + ", name=" + name + ", description="
				+ description + ", alternateNames=" + alternateNames
				+ ", resourceId=" + resourceId + ", compositePropertyKey="
				+ compositePropertyKey + ", instanceId=" + instanceId + "]";
	}
}
