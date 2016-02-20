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

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.comicwiki.IRI;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectDate;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ObjectURL;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;

import com.google.common.collect.Lists;

import static org.comicwiki.rdf.RdfFactory.*;

/**
 * Transforms a Thing into a collection of Statements.
 */
public final class ThingToStatementsTransformer {

	private static Collection<Statement> createStatements(
			RdfSubject rdfSubject, Field field, Object instance)
			throws Exception {
		Collection<Statement> statements = Lists.newArrayList();

		Annotation predicateAnnotation = field.getAnnotation(Predicate.class);
		if (predicateAnnotation == null) {
			return statements;
		}
		String predicateIRI = ((Predicate) predicateAnnotation).value();
		RdfPredicate rdfPredicate = createRdfPredicate(new IRI(predicateIRI));

		Object fieldInstance = field.get(instance);
		if (fieldInstance == null) {
			return statements;
		}
		for (Annotation annotation : field.getAnnotations()) {
			if (annotation instanceof ObjectIRI) {
				if (fieldInstance instanceof Collection) {
					Collection<IRI> c = (Collection<IRI>) fieldInstance;
					for (IRI value : c) {
						RdfObject rdfObject = createRdfObject(value);
						statements.add(new Statement(rdfSubject, rdfPredicate,
								rdfObject));
					}
				} else if (fieldInstance instanceof IRI) {
					IRI value = (IRI) fieldInstance;
					RdfObject rdfObject = createRdfObject(value);
					statements.add(new Statement(rdfSubject, rdfPredicate,
							rdfObject));
				} else if (Thing.class.isAssignableFrom(fieldInstance
						.getClass())) {
					IRI value = ((Thing) fieldInstance).resourceId;
					RdfObject rdfObject = createRdfObject(value);
					statements.add(new Statement(rdfSubject, rdfPredicate,
							rdfObject));
				} else {
					throwMismatchException(field);
				}
			} else if (annotation instanceof ObjectString) {
				if (fieldInstance instanceof Collection) {
					Collection<String> c = (Collection<String>) fieldInstance;
					for (String value : c) {
						statements.add(new Statement(rdfSubject, rdfPredicate,
								value));
					}
				} else if (fieldInstance instanceof String) {
					String value = (String) fieldInstance;
					statements.add(new Statement(rdfSubject, rdfPredicate,
							value));
				} else if (fieldInstance instanceof Enum) {
					String value = ((Enum<?>) fieldInstance).name();
					statements.add(new Statement(rdfSubject, rdfPredicate,
							value));
				} else {
					throwMismatchException(field);
				}
			} else if (annotation instanceof ObjectBoolean) {
				if (!(fieldInstance instanceof Boolean)) {
					throwMismatchException(field);
				}
				Boolean value = (Boolean) fieldInstance;
				statements.add(new Statement(rdfSubject, rdfPredicate, value));

			} else if (annotation instanceof ObjectDate) {
				if (!(fieldInstance instanceof Date)) {
					throwMismatchException(field);
				}
				// TODO: implement date (formatter)
			} else if (annotation instanceof ObjectNumber) {
				if (!(fieldInstance instanceof Number)) {
					throwMismatchException(field);
				}
				Number value = (Number) fieldInstance;
				statements.add(new Statement(rdfSubject, rdfPredicate, value));

			} else if (annotation instanceof ObjectURL) {
				if (!(fieldInstance instanceof URL)) {
					throwMismatchException(field);
				}
				URL value = (URL) fieldInstance;
				statements.add(new Statement(rdfSubject, rdfPredicate, value));
			} else if (annotation instanceof ObjectXSD) {
				statements.add(new Statement(rdfSubject, rdfPredicate,
						(ObjectXSD) annotation, fieldInstance));
			} else if (annotation instanceof ObjectNonNegativeInteger) {
				if (!(fieldInstance instanceof Integer)) {
					throwMismatchException(field);
				}
				statements.add(new Statement(rdfSubject, rdfPredicate,
						(ObjectNonNegativeInteger) annotation, (Integer) fieldInstance));
			}
		}
		return statements;
	}

	private static Statement subject(Subject subject, IRI resourceId) {
		RdfSubject rdfSubject = createRdfSubject(resourceId);
		RdfPredicate rdfPredicate = createRdfPredicate(new IRI(
				DataType.RDF_TYPE));
		RdfObject rdfObject = createRdfObject(new IRI(subject.value()));

		return new Statement(rdfSubject, rdfPredicate, rdfObject);
	}

	private static void throwMismatchException(Field field) {
		throw new IllegalArgumentException(
				"Annotated type does not match Java field type: "
						+ field.getName());
	}

	public static Collection<Statement> transform(Thing thing) throws Exception {
		checkNotNull(thing, "thing");
		Collection<Statement> statements = new ArrayList<>();
		Class<?> clazz = thing.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);

		IRI id = thing.resourceId;
		if (id == null) {
			throw new IllegalArgumentException("thing.resourceId is empty");
		}

		statements.add(subject(subject, id));

		RdfSubject rdfSubject = createRdfSubject(id);
		for (Field field : thing.getClass().getFields()) {
			field.setAccessible(true);
			statements.addAll(createStatements(rdfSubject, field, thing));
		}
		return statements;

	}
}
