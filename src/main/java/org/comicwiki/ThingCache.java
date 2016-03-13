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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.Subject;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class ThingCache {

	private final KeyIDGenerator instanceIDGen = new KeyIDGenerator(0);

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
			if (sb.length() == 0) {
				return "empty";
				//throw new IllegalThingException("No values for any key: "
				//		+ clazz.getName());
			}
			return DigestUtils.md5Hex(object.getClass().getCanonicalName()
					+ ":" + sb.toString());
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
		return null;
	}

	protected final String readCompositePropertyKey(Thing object) {
		Class<?> clazz = object.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);
		try {
			return readCompositePropertyKey(subject, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//protected final HashMap<IRI, Thing> instanceCache = new HashMap<>(Integer.MAX_VALUE);

	protected final HashMapShard instanceCache  = new HashMapShard(3);
	
	private final Repositories repositories;

	private final IRICache iriCache;

	private final ResourceIDCache resourceIDCache;

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

	public synchronized void assignResourceIDs() {
		HashMap<IRI, String> instanceCpkMap = new HashMap<>(1000000);
		HashMap<IRI, IRI> instanceResourceMap = new HashMap<>(1000000);
		for (Thing thing : instanceCache.values()) {
			Subject subjectAnnotation = (Subject) thing.getClass()
					.getAnnotation(Subject.class);
			if (subjectAnnotation.isBlankNode()) {
				thing.resourceId = new IRI(
						resourceIDCache.generateAnonymousId());
				thing.compositePropertyKey = thing.resourceId.value;
				instanceCpkMap
						.put(thing.instanceId, thing.compositePropertyKey);
				resourceIDCache.put(thing.compositePropertyKey,
						thing.resourceId);
			} else {
				thing.compositePropertyKey = readCompositePropertyKey(thing);
				if (Strings.isNullOrEmpty(thing.compositePropertyKey)) {
				//	throw new IllegalArgumentException(
				System.out.println("ETL: " +			"thing.compositePropertyKey is empty: "
									+ thing.getClass() + ", thing.name=" + thing.name + ":" + thing);
				}
				instanceCpkMap
						.put(thing.instanceId, thing.compositePropertyKey);

				if (!resourceIDCache.containsKey(thing.compositePropertyKey)) {
					thing.resourceId = new IRI(
							resourceIDCache.generateResourceId());
					resourceIDCache.put(thing.compositePropertyKey,
							thing.resourceId);
				} else {
					thing.resourceId = resourceIDCache
							.get(thing.compositePropertyKey);
				}
			}

			instanceResourceMap.put(thing.instanceId, thing.resourceId);
		}
		instanceCpkMap.clear();

		for (IRI iri : iriCache.values()) {
			iri.value = instanceResourceMap.get(iri).value;
		}
	}

	public void exportToRepositories() {
		for (Thing thing : instanceCache.values()) {
			Repository<Thing> repo = repositories.getRepository(thing
					.getClass());
			if(repo != null) {
				repo.add(thing);
			}		
		}
	}
}
