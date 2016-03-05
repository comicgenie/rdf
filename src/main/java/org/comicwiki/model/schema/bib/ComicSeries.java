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
package org.comicwiki.model.schema.bib;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.model.schema.Periodical;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaBib;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/ComicSeries", key= "name")
@SchemaBib
public class ComicSeries extends Periodical {

	//use URL to reference http://comics.org/issues/id
	
	@Predicate("binding")
	@ObjectString
	@SchemaComicWiki
	public Collection<String> binding  = new HashSet<>(3);
	
	@Predicate("color")
	@ObjectString
	@SchemaComicWiki
	public Collection<String> colors  = new HashSet<>(3);
	
	@Predicate("dimension")
	@ObjectString
	@SchemaComicWiki
	public Collection<String> dimensions  = new HashSet<>(3);
	
	@Predicate("format")
	@ObjectString
	@SchemaComicWiki
	public Collection<String> format  = new HashSet<>(3);
	
	@Predicate("paperStock")
	@ObjectString
	@SchemaComicWiki
	public Collection<String> paperStock  = new HashSet<>(3);
	
}
