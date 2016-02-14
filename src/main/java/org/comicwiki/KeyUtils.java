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

import org.apache.commons.codec.digest.DigestUtils;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.Subject;

public final class KeyUtils {

	public static String readResouceIDWithExpandedIri(Thing object) {
		return ResourceUtils.expandIri(object.resourceId);
	}

	public static String readCompositePropertyKey(Thing object) {
		Class<?> clazz = object.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);
		try {
			return readCompositePropertyKey(subject, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String readCompositePropertyKey(Subject subject, Thing object)
			throws NoSuchFieldException, SecurityException,
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
				sb.append("_");
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
}
