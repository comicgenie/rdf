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
package org.comicwiki.gcd;

import java.util.Collection;
import java.util.HashSet;

import org.apache.spark.sql.Row;
import org.comicwiki.IRICache;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.schema.Person;

public final class CreatorFieldParser implements FieldParser<Collection<Person>> {

	private final ThingFactory thingFactory;
	private final IRICache iriCache;

	public CreatorFieldParser(ThingFactory thingFactory, IRICache iriCache) {
		this.thingFactory = thingFactory;
		this.iriCache = iriCache;
	}
	
	@Override
	public Collection<Person> parse(int field, Row row) {
		//parse for ;
		//create a person, create alias(penName), role?
		Person person = thingFactory.create(Person.class);
		person.name =  row.getString(field);
		
		Collection<Person> creators = new HashSet<Person>();
		creators.add(person);
		return creators;
		//Put into INSTANCE CACHE
		//remove typeset
		//return value.replaceAll("\\(.*?\\)", "").replaceAll("?", "").trim();
	}
	
	//Initials of
	//Variation of name
	//Differenr name
	/**
	 *  Joe Kubert
* C. C. Beck
* Joe Kubert ?
* Gene Colan [as Adam Austin]
* ? [as A. Nony Mouse]
* Richard Starkings; Comicraft; Saida Temofonte [as ST]; Eric Eng Wong [as EEW] 
* Diverse Hands; Frank Giacoia; Mike Esposito; Alan Weiss
* Vince Colletta; Murphy Anderson (Superman heads)
* Jack Kirby (page 1, pages 20-22); Alex Toth (pages 2-19)
* Jack Kirby (p. 1, pp. 20-22); Alex Toth (pp. 2-19)
* Julius Schwartz; E. Nelson Bridwell (assistant)
* Wendy Pini; Richard Pini; Wolfgang Hohlbein (translator)
* Stan Lee (plot); Dennis O'Neil [as Sergius O'Shaugnessy] (script)
* William Shakespeare (original story); Fred Hembeck (adaptation)
* Sam Glanzman [as S.J.G] (signed) (real name [as initials] with signature confirmation)
* Frank Giacoia (see notes)
	 */
	protected String parse(String field) {
		return null;
	}

}
