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

import org.comicwiki.Add;
import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

/**
 * A brand is a name used by an organization or business person for labeling a
 * product, product group, or similar.
 */
@Subject(value = "http://schema.org/Brand", key = "name")
public class Brand extends Intangible {

	@Predicate("brandUse")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] brandUse;
	
	@Predicate("endUseDate")
	@ObjectIRI
	@SchemaComicWiki
	public IRI endUseDate;
	
	@Predicate("parentBrand")
	@ObjectIRI
	@SchemaComicWiki
	public IRI parentBrand;
	
	@Predicate("publisher")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] publisher;;
	
	@Predicate("startUseDate")
	@ObjectIRI
	@SchemaComicWiki
	public IRI startUseDate;
	
	@Predicate("subBrand")
	@ObjectIRI
	@SchemaComicWiki
	public IRI[] subBrand;
	
	public void addSubBrand(IRI brand) {
		subBrand = Add.one(subBrand, brand);
	}
	
	public void addPublisher(IRI pub) {
		publisher = Add.one(publisher, pub);
	}
	
	public void addBrandUse(IRI use) {
		brandUse = Add.one(brandUse, use);
	}
}
