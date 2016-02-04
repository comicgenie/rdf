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
package org.comicwiki.gcdb;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ComicEntity {

	private Collection<ComicOrganization> organizations = new HashSet<>();
	
	private Collection<ComicCharacter> characters = new HashSet<>();
	
	public ComicEntity() { }
	
	public ComicEntity(Collection<ComicOrganization> organizations, Collection<ComicCharacter> characters ) {
		this.organizations = organizations;
		this.characters = characters;
	}
	
	public Collection<ComicCharacter> getCharacters() {
		return characters;
	}

	public void setCharacters(Collection<ComicCharacter> characters) {
		this.characters = characters;
	}

	public void add(ComicOrganization organization) {
		organizations.add(organization);
	}

	public Collection<ComicOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Collection<ComicOrganization> organizations) {
		this.organizations = organizations;
	}
	
}
