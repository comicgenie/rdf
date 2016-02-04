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

import java.util.HashMap;

import org.comicwiki.model.OrganizationType;

public class OrganizationMapper {
	
	public static final HashMap<String, OrganizationType> ORGANIZATION = new HashMap<>();;
	
	static {
		for(OrganizationType org : OrganizationType.values()) {
			ORGANIZATION .put(org.name(), org);
		}
		ORGANIZATION .put("corp.", OrganizationType.corporation);
	}
	/*
	public static final ImmutableMap<String, OrganizationType> ORGANIZATION = new ImmutableMap.Builder<String, OrganizationType>()
			.put("academy", OrganizationType.academy)
			.put("agency", OrganizationType.agency)
			.put("alliance", OrganizationType.alliance)
			.put("academy", OrganizationType.army)
			.put("academy", OrganizationType.academy)
			.build();
			*/

}
