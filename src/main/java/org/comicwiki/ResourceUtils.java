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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.comicwiki.model.schema.Thing;

import com.google.common.base.Strings;

public class ResourceUtils {

	public final static String BASE_URI = "http://comicwiki.org/resources/";

	public static String readResourceIDWithExpandedIri(Thing thing) {
		String rid = thing.resourceId;
		if(thing == null || Strings.isNullOrEmpty(rid)){
			throw new IllegalArgumentException(
					"Thing null or missing resourceId");
		}
		if (!rid.startsWith("@")) {
			throw new IllegalArgumentException(
					"Thing resourceId must start with '@'");
		}
		return expandIri(rid.substring(1, rid.length()));
	}

	public static String expandIri(String iri) {
		try {
			return isAbsolute(iri) ? iri : BASE_URI
					+ URLEncoder.encode(iri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	private static boolean isAbsolute(String iri) {
		return iri.startsWith("http");
	}
}
