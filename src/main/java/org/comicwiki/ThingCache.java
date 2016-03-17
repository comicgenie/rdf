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

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.Subject;

import com.google.common.base.Strings;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class ThingCache {

	private static final Logger LOG = Logger.getLogger("ETL");

	private final KeyIDGenerator instanceIDGen = new KeyIDGenerator(0);
	
	private HashFunction hf = Hashing.goodFastHash(64);

	private IRI assignInstanceId(Thing thing) {
		if (thing.instanceId == null) {
			thing.instanceId = IRI.create(
					"-" + instanceIDGen.createInstanceId(), iriCache);
		}
		return thing.instanceId;
	}

	protected final String readCompositePropertyKey(Subject subject,
			Thing object) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException,
			IllegalThingException {
		Class<?> clazz = object.getClass();
		if (subject.key().isEmpty()) {// composite
			StringBuilder sb = new StringBuilder();
			String[] keys = subject.compositeKey();

			for (String key : keys) {
				Field field = clazz.getField(key);
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (fieldValue == null) {
					continue;
				} else if (fieldValue instanceof String) {
					sb.append(field.getName()).append(":").append(fieldValue);
				} else if (fieldValue instanceof IRI) {
					Thing thing = instanceCache.get((IRI) fieldValue);
					String id = readCompositePropertyKey(thing);
					sb.append(id);
				}
				sb.append('_');
			}
			if (sb.length() > 0) {
				return hf.hashBytes((object.getClass().getCanonicalName()
						+ ":" + sb.toString()).getBytes()).toString();
			//	return DigestUtils.md5Hex();
			}
		} else {
			Field field = clazz.getField(subject.key());
			field.setAccessible(true);
			Object fieldValue = field.get(object);
			if (fieldValue instanceof String) {
				return DigestUtils.md5Hex((String) fieldValue);
			} else if (fieldValue == null) {
				return null;
			} else if (Thing.class.isAssignableFrom(fieldValue.getClass())) {
				return readCompositePropertyKey((Thing) fieldValue);
			}
		}
		LOG.warning("This class does not have a key assigned. Is it a blank node? "
					+ clazz.getName());
	
		return null;
	}

	protected final String readCompositePropertyKey(Thing object) {
		Class<?> clazz = object.getClass();
		try {
			return readCompositePropertyKey(clazz.getAnnotation(Subject.class), object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected HashMap<IRI, Thing> instanceCache = new HashMap<>(1000000);

	// protected final HashMapShard instanceCache = new HashMapShard(3);

	private final Repositories repositories;

	private final IRICache iriCache;

	private ResourceIDCache resourceIDCache;

	@Inject
	public ThingCache(Repositories repositories, IRICache iriCache,
			ResourceIDCache resourceIDCache) {
		this.repositories = repositories;
		this.iriCache = iriCache;
		this.resourceIDCache = resourceIDCache;
	}

	public Thing get(IRI iri) {
		return instanceCache.get(iri);
	}

	public Collection<Thing> getThings() {
		return instanceCache.values();
	}

	public void add(Thing thing) {
		assignInstanceId(thing);
		instanceCache.put(thing.instanceId, thing);
	}

	private void setResourceIdOnBlank(Thing thing) {
		thing.resourceId = new IRI(resourceIDCache.generateAnonymousId());
		thing.compositePropertyKey = thing.resourceId.value;
	}

	private void setResourceIdOn(Thing thing) {
		IRI thingResourceId = resourceIDCache.get(thing.compositePropertyKey);
		if (thingResourceId != null) {
			thing.resourceId = thingResourceId;
		} else {
			thing.resourceId = new IRI(resourceIDCache.generateResourceId());
			resourceIDCache.put(thing.compositePropertyKey, thing.resourceId);
		}
	}

	public void assignResourceIDs() throws IOException {
		@SuppressWarnings("resource")
		FileOutputStream iriOut = new FileOutputStream("iri.txt");
	//	FileOutputStream resOut = new FileOutputStream("resourceIds2.txt");
		int count = 0;
		//Iterator<Thing> it = instanceCache.values().iterator();
	//	while(it.hasNext()) {
			//Thing thing = it.next();
		//	it.remove();
		//}
		for (Thing thing : instanceCache.values()) {
			if (count++ % 1000000 == 0) {
				LOG.info("Rows with assigned IDs: " + count);
			}
			Subject subjectAnnotation = (Subject) thing.getClass()
					.getAnnotation(Subject.class);
			if (subjectAnnotation.isBlankNode()) {
				setResourceIdOnBlank(thing);
			} else {
				thing.compositePropertyKey = readCompositePropertyKey(thing);
				if (Strings.isNullOrEmpty(thing.compositePropertyKey)) {
					throw new IllegalArgumentException(
					// System.out.println("ETL: " +
							"thing.compositePropertyKey is empty: "
									+ thing.getClass() + ", thing.name="
									+ thing.name + ":" + thing);
				}
				setResourceIdOn(thing);
			}
			// swap instance id for resourceId in cache
			IRI v = iriCache.get(thing.instanceId.value);
			v.value = thing.resourceId.value;
			iriOut.write((v.value + "\r\n").getBytes());	
			/*
			Repository<Thing> repo = repositories.getRepository(thing
					.getClass());
			if (repo != null) {
				repo.add(thing);		
			} else {
				//Blank nodes repo?					
			}	
			*/			
			//it.remove();
		}
		iriCache.clear();
		iriOut.close();
	}

	public void clear() {
		instanceCache.clear();
		instanceCache = null;
	//	iriCache.clear();
	}
	
	public void exportToRepositories() {
		//iterate and remove as add
		int count = 0;
	//	Iterator<Thing> it = instanceCache.values().iterator();
		//while(it.hasNext()) {
			//Thing thing = it.next();
		//	it.remove();
		for (Thing thing : instanceCache.values()) {
			Repository<Thing> repo = repositories.getRepository(thing
					.getClass());
			if (repo != null) {
				repo.add(thing);		
			} else {
				//Blank nodes repo?					
			}		
			
			if (count++ % 100000 == 0) {
				LOG.info("Exported thing " + count);
			}
		}
	}
}
