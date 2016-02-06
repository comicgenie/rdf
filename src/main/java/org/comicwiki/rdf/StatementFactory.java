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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.comicwiki.KeyUtils;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;

import com.google.common.collect.Lists;

public final class StatementFactory {

	public static Statement createSubjectStatement(Subject subject,
			String compositeId) {
		RdfSubject rdfSubject = Statement.createRdfSubject(compositeId);
		RdfPredicate rdfPredicate = Statement
				.createRdfPredicate(DataTypeConstants.RDF_TYPE);
		RdfObject rdfObject = Statement.createRdfObjectIRI(URI.create(subject
				.value()));
		Statement statement = new Statement(rdfSubject, rdfPredicate, rdfObject);

		return statement;
	}

	public static Collection<Statement> from(Object object) throws Exception {
		Collection<Statement> statements = new ArrayList<>();
		Class<?> clazz = object.getClass();
		Subject subject = clazz.getAnnotation(Subject.class);
		String id = KeyUtils.readKey(subject, object);

		statements.add(createSubjectStatement(subject, id));
		
		RdfSubject rdfSubject = Statement.createRdfSubject(id);
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			statements.addAll(objects(rdfSubject, field, object));
			// statements.addAll(process(subject, field));
		}
		return statements;

	}

	private static Collection<Statement> objects(RdfSubject rdfSubject, Field field,
			Object instance) throws Exception {
		Collection<Statement> statements = Lists.newArrayList();
		
		Annotation predicateAnnotation = field.getAnnotation(Predicate.class);
		if(predicateAnnotation == null) {
			return statements;
		}
		String predicateIRI = ((Predicate) predicateAnnotation).value();		
		RdfPredicate rdfPredicate = Statement.createRdfPredicate(predicateIRI);
		
		Object fieldInstance = field.get(instance);
		
		for (Annotation annotation : field.getAnnotations()) {
			if (annotation instanceof ObjectIRI) {
				if (fieldInstance instanceof Collection) {
					Collection<String> c = (Collection<String>) fieldInstance;
					for(String value : c) {
						RdfObject rdfObject = Statement.createRdfObjectIRI(URI.create(value));
						statements.add( new Statement(rdfSubject, rdfPredicate, rdfObject));
					}	
				} else if(fieldInstance instanceof String) {
					String value = (String) fieldInstance;
					RdfObject rdfObject = Statement.createRdfObjectIRI(URI.create(value));
					statements.add( new Statement(rdfSubject, rdfPredicate, rdfObject));
				} else if(Thing.class.isAssignableFrom(fieldInstance.getClass())) {				
					String value = KeyUtils.readKey(fieldInstance);
					RdfObject rdfObject = Statement.createRdfObjectIRI(URI.create(value));
					statements.add( new Statement(rdfSubject, rdfPredicate, rdfObject));
				}
			} else if(annotation instanceof ObjectString) {
				if (fieldInstance instanceof Collection) {
					Collection<String> c = (Collection<String>) fieldInstance;
					for(String value : c) {					
						statements.add( new Statement(rdfSubject, rdfPredicate, value));
					}	
				} else if(fieldInstance instanceof String) {
					String value = (String) fieldInstance;
					statements.add( new Statement(rdfSubject, rdfPredicate, value));
				} else if(fieldInstance instanceof Enum) {
					String value = ((Enum<?>) fieldInstance).name();
					statements.add( new Statement(rdfSubject, rdfPredicate, value));
				}
			}
		}
		return statements;
	}
}
