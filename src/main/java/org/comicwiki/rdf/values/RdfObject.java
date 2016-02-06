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
package org.comicwiki.rdf.values;

import org.comicwiki.rdf.StatementItemType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class RdfObject {
	
	private StatementItemType type = StatementItemType.IRI;
	
	private String language ="en";
	
	private String value;//literal or IRI
	
	//literal datatype: string, int
	/**
	 * rdf:type owl:DatatypeProperty -> literal
rdf:type owl:ObjectProperty -> URI (property)

float -> QuntityValue
integer -> QuntityValue
boolean -> QuntityValue
duration  -> QuntityValue

anyURI -> URL
string  -> string

dateTime -> timeValue
date ->timeValue

	 */
	//"http://www.w3.org/1999/02/22-rdf-syntax-ns#langString - literal only
	private String datatype;
	
	public RdfObject() { }
	
	public RdfObject(String value, StatementItemType type, String datatype, String language) {
		this.value = value;
		this.type = type;
		this.datatype = datatype;
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public String getValue() {
		return value;
	}

	public String getDatatype() {
		return datatype;
	}

	public StatementItemType getType() {
		return type;
	}

	public void setType(StatementItemType type) {
		this.type = type;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datatype == null) ? 0 : datatype.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RdfObject other = (RdfObject) obj;
		if (datatype == null) {
			if (other.datatype != null)
				return false;
		} else if (!datatype.equals(other.datatype))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}