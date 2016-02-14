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
package org.comicwiki.serializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.comicwiki.model.CreativeWorkExtension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CreativeWorkDeserializer extends StdDeserializer<CreativeWorkExtension> {

	protected CreativeWorkDeserializer() {
		super(CreativeWorkExtension.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8998268740787798503L;

	@Override
	public CreativeWorkExtension deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) p.getCodec();
		return mapper.readValue(p, CreativeWorkExtension.class);
		
	}

}