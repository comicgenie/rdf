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

import java.net.URI;
import java.net.URL;

import org.comicwiki.ResourceUtils;
import org.comicwiki.rdf.annotations.ObjectInteger;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;

public class Statement implements Comparable<Statement> {

	public static RdfObject createRdfObjectIRI(URI iri) {
		return new RdfObject(ResourceUtils.expandIri(iri.toString()),
				StatementItemType.IRI, null, null);
	}

	public static RdfPredicate createRdfPredicate(String iri) {
		return new RdfPredicate(ResourceUtils.expandIri(iri));
	}

	public static RdfSubject createRdfSubject(String iri) {
		return new RdfSubject(ResourceUtils.expandIri(iri));
	}

	private RdfObject object;

	private RdfPredicate predicate;

	private RdfSubject subject;

	public Statement() {
	}

	public Statement(RdfSubject subject, RdfPredicate predicate,
			RdfObject rdfObject) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = rdfObject;
	}

	private Statement(Subject subject, Predicate predicate) {
		this.subject = createRdfSubject(subject.value());
		this.predicate = createRdfPredicate(predicate.value());
	}

	public Statement(Subject subject, Predicate predicate, boolean value) {
		this(subject, predicate);
		this.object = createRdfObjectLiteral(value);
	}

	public Statement(Subject subject, Predicate predicate, Number value) {
		this(subject, predicate);
		this.object = createRdfObjectLiteral(value);
	}

	public Statement(Subject subject, Predicate predicate, ObjectInteger value) {
		this(subject, predicate);
		// this.object = createRdfObjectLiteral(value);
	}

	public Statement(RdfSubject subject, RdfPredicate predicate, String string) {
		// this(subject, predicate);
		this.subject = subject;
		this.predicate = predicate;
		this.object = createRdfObjectLiteral(string);
	}

	public Statement(Subject subject, Predicate predicate, String string) {
		this(subject, predicate);
		this.object = createRdfObjectLiteral(string);
	}

	public Statement(Subject subject, Predicate predicate, URI uri) {
		this(subject, predicate);
		this.object = createRdfObjectIRI(uri);
	}

	public Statement(Subject subject, Predicate predicate, URL value) {
		this(subject, predicate);
		this.object = createRdfObjectLiteral(value);
	}

	@Override
	public int compareTo(Statement o) {
		if (o == null || o.getPredicate() == null) {
			return -1;
		} else if (o.getPredicate().equals(
				"http://www.w3.org/2000/01/rdf-schema#label")) {
			return 1;
		}
		return 0;
	}

	private RdfObject createRdfObjectLiteral(boolean value) {
		String datatype = "http://www.w3.org/2001/XMLSchema#boolean";
		return new RdfObject(String.valueOf(value), StatementItemType.literal,
				datatype, null);
	}

	private RdfObject createRdfObjectLiteral(Number value) {
		String datatype = "http://www.w3.org/2001/XMLSchema#integer";
		return new RdfObject(value.toString(), StatementItemType.literal,
				datatype, null);
	}

	private RdfObject createRdfObjectLiteral(String value) {
		// "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString"
		String datatype = "http://www.w3.org/2001/XMLSchema#string";
		return new RdfObject(value, StatementItemType.literal, datatype, null);
	}

	private RdfObject createRdfObjectLiteral(URL value) {
		String datatype = "http://www.w3.org/2001/XMLSchema#anyURI";
		return new RdfObject(value.toString(), StatementItemType.literal,
				datatype, null);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statement other = (Statement) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	public RdfObject getObject() {
		return object;
	}

	public RdfPredicate getPredicate() {
		return predicate;
	}

	public RdfSubject getSubject() {
		return subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	public void setObject(RdfObject object) {
		this.object = object;
	}

	public void setObjectValue(String value) {

	}

	public void setPredicate(RdfPredicate predicate) {
		this.predicate = predicate;
	}

	public void setSubject(RdfSubject subject) {
		this.subject = subject;
	}
}
