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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.Subject;

import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;

public final class ThingCache {

	private static final HashBiMap<String, String> cpkResourceMap = HashBiMap
			.create();

	private static final KeyIDGenerator instanceIDGen = new KeyIDGenerator(0);

	private static final KeyIDGenerator resourceIDGen = new KeyIDGenerator(0);

	protected static final HashMap<String, Thing> sInstanceCache = new HashMap<>(
			1000000);

	public static void add(Thing thing) {
		assignInstanceId(thing);
		sInstanceCache.put(thing.instanceId, thing);
	}

	private static String assignInstanceId(Thing thing) {
		if (Strings.isNullOrEmpty(thing.instanceId)) {
			thing.instanceId = "-" + instanceIDGen.createInstanceId();
		}
		return thing.instanceId;
	}

	public static void load() {
		for (Thing thing : sInstanceCache.values()) {
			Repository<Thing> repo = Repositories.getRepository(thing
					.getClass());
			repo.add(thing);
		}
	}

	public static synchronized void assignResourceIDs() {
		HashMap<String, String> instanceCpkMap = new HashMap<>(1000000);
		HashMap<String, String> instanceResourceMap = new HashMap<>(1000000);
		for (Thing thing : sInstanceCache.values()) {
			thing.compositePropertyKey = readCompositePropertyKey(thing);
			instanceCpkMap.put(thing.instanceId, thing.compositePropertyKey);

			if (!cpkResourceMap.containsKey(thing.compositePropertyKey)) {
				thing.resourceId = generateResourceId();
				cpkResourceMap
						.put(thing.compositePropertyKey, thing.resourceId);
			} else {
				thing.resourceId = cpkResourceMap
						.get(thing.compositePropertyKey);
			}

			instanceResourceMap.put(thing.instanceId, thing.resourceId);
		}
		instanceCpkMap.clear();

		for (IRI iri : IRICache.values()) {
			iri.value = instanceResourceMap.get(iri.value);
		}
	}

	public static void exportResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
	}

	private static String generateResourceId() {
		return "@" + resourceIDGen.createID();
	}

	public static void loadResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
	}

	protected final static String readCompositePropertyKey(Subject subject,
			Thing object) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		if (subject.key().isEmpty()) {// composite
			StringBuilder sb = new StringBuilder();
			String[] keys = subject.compositeKey();

			for (String key : keys) {
				Field field = clazz.getField(key);
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (fieldValue instanceof String) {
					sb.append(fieldValue);
				} else if (fieldValue == null) {

				} else if (Thing.class.isAssignableFrom(fieldValue.getClass())) {
					String id = readCompositePropertyKey((Thing) fieldValue);
					sb.append(id);
				}
				sb.append('_');
			}
			return DigestUtils.md5Hex(sb.toString());
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

	protected final static String readCompositePropertyKey(Thing object) {
		Class<?> clazz = object.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);
		try {
			return readCompositePropertyKey(subject, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
