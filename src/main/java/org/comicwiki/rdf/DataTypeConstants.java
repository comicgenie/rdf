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
package org.comicwiki.rdf;

public class DataTypeConstants {
	
	private static final String xsd = "http://www.w3.org/2001/XMLSchema#";
	
	public static final String STRING = xsd + "string";
	
	public static final String INTEGER = xsd + "integer";
	
	public static final String BOOLEAN = xsd + "boolean";
	
	public static final String URI = xsd + "uri";
	
	public static final String DATE = xsd + "date";
	
	public static final String LANGUAGE_STRING  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString";
	
	public static final String RDF_TYPE  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
}
