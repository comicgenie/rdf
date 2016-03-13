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

import org.comicwiki.IRI;
import org.comicwiki.model.schema.Person;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ParentClass;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ComicCharacter", compositeKey= {"name", "universe", "era"})
public class ComicCharacter extends Person  {

	@Predicate("ability")
	@ObjectString
	public Collection<String> abilities;
	
	@Predicate("alignment")
	@ObjectString
	public Alignment alignment;
	
	@ParentClass
	public ComicCreativeWork creativeWork = new ComicCreativeWork();
	
	@Predicate("era")
	@ObjectString
	public String era;

	@Predicate("identity")
	@ObjectIRI
	public Collection<IRI> identities;
	
	@Predicate("identityType")
	@ObjectString
	public IdentityType identityType;

	@Predicate("ComicUniverse")
	@ObjectIRI
	public IRI universe;

	public ComicCharacter() { }

	public void addAbility(String ability) {
		if(abilities == null) {
			abilities = new HashSet<>(3);
		}
		abilities.add(ability);
	}
	
	public void addIdentity(IRI identity) {
		if(identities == null) {
			identities = new HashSet<>(3);
		}
		identities.add(identity);
	}

}
