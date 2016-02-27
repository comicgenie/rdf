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
package org.comicwiki;

import org.comicwiki.model.schema.Thing;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ThingFactory {

	private ThingCache thingCache;

	@Inject
	public ThingFactory(ThingCache thingCache) {
		this.thingCache = thingCache;
	}
	
	public ThingCache getCache() {
		return thingCache;
	}
	
	public <T extends Thing> T create(Class<T> clazz) {
		try {
			Thing thing = (Thing) clazz.newInstance();
			thingCache.add(thing);
			return (T) thing;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
}
