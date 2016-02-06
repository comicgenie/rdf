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

import org.comicwiki.rdf.annotations.Subject;

public class KeyUtils {
	
	public static String readKeyWithExpandedIri(Object object) {
		String key = readKey(object);
		return ResourceUtils.expandIri( key );		
	}
	
	public static String readKey(Object object) {
		Class<?> clazz = object.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);
		try {
			return readKey(subject, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readKey(Subject subject, Object object)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		if (subject.key().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			String[] keys = subject.compositeKey();

			for (String key : keys) {
				Field field = clazz.getField(key);
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (fieldValue instanceof String) {
					sb.append(fieldValue);
				} else if (fieldValue == null) {

				} else {
					String id = readKey(fieldValue);
					sb.append(id);
				}
				sb.append("_");
			}
			return sb.toString().replaceAll("(_)*$", "");

		} else {
			Field field = clazz.getField(subject.key());
			field.setAccessible(true);
			Object fieldValue = field.get(object);
			if (fieldValue instanceof String) {
				return (String) fieldValue;
			} else if (fieldValue == null) {
				return "";
			} else {
				return readKey(fieldValue);
			}
		}
	}
}
