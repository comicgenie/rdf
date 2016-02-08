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
package org.comicwiki.repositories;

import java.io.IOException;

import org.comicwiki.BaseRepository;
import org.comicwiki.model.schema.Person;

public class CreatorRepository extends BaseRepository<Person>{

	@Override
	public Person merge(Person source, Person target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
