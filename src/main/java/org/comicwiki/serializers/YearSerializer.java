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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class YearSerializer extends StdSerializer<Date> {

	protected YearSerializer() {
		super(Date.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1773603738538548563L;

	@Override
	public void serialize(Date value, JsonGenerator gen,
			SerializerProvider provider) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);

		String prefix = calendar.get(Calendar.ERA) == GregorianCalendar.BC ? "-"
				: "+";
		SimpleDateFormat f = new SimpleDateFormat(
				"yyyy");
		gen.writeString(prefix + f.format(calendar.getTime()));
	}

}
