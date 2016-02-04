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
package org.comicwiki.model.schema;

import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/Periodical", key= "name")
public class Periodical extends CreativeWorkSeries {

	@Predicate("issn")
	@ObjectString
	public String issn;
	
	//use hasPart for PublicationVolume
	
	//(ComicSeries:Periodical[CreativeWork.hasPart] -> PublicationVolume.hasPart -> ComicIssue
	//Create IRI for Series: Volume???? [what if no volume]
	
	//frequency??? gcd_issue.indicia_frequency (text)
}
