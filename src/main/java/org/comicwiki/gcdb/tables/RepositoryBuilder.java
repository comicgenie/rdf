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
package org.comicwiki.gcdb.tables;

import java.io.File;
import java.io.IOException;

import org.comicwiki.gcdb.NamesImporter;
import org.comicwiki.gcdb.repositories.ComicCharacterRepository;
import org.comicwiki.model.schema.ComicStory;

public class RepositoryBuilder {

	public void build(StoryTable table) throws IOException {
		// iterate table
		ComicCharacterRepository repo2 = new ComicCharacterRepository();
		for (StoryTable.StoryRow storyRow : table.cache.values()) {

			repo2.add(storyRow.characters);

			// storyRow.characters - add to charactersRepo
			// and so on

			// construct ComicStory from row
			ComicStory comicStory = new ComicStory();
			comicStory.name = storyRow.title;
			// add to Repo
		}
		NamesImporter imp = new NamesImporter();
		imp.load(new File("yob2014.txt"));
		imp.loadLastNames(new File("lastname.txt"));
		repo2.addGender(imp.maleCache, imp.femaleCache, imp.lastNamesCache);
		repo2.save(new File("character.repo.txt"));
	}
}
