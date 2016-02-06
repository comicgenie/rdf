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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.comicwiki.ResourceUtils;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.Subject;
import org.junit.Test;

public class StatementTest {

	@Test
	public void iriObject() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);
		ObjectIRI object = mock(ObjectIRI.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("identity");
		/*
		 * when(object.value()).thenReturn(Statement.BASE_URI + "Clark+Kent");
		 * 
		 * Statement statement = new Statement(subject, predicate, object);
		 * 
		 * assertEquals(StatementItemType.IRI, statement.getObject().getType());
		 * assertEquals(Statement.BASE_URI + "Clark+Kent",
		 * statement.getObject().getValue());
		 */
	}

	@Test
	public void predicate() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("identity");

		Statement statement = new Statement(subject, predicate, "Clark");

		assertEquals(StatementItemType.IRI, statement.getPredicate().getType());
		assertEquals(ResourceUtils.BASE_URI + "identity", statement
				.getPredicate().getValue());
	}

	@Test
	public void predicateWithSpaces() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("secret identity");

		Statement statement = new Statement(subject, predicate, "Clark");

		assertEquals(StatementItemType.IRI, statement.getPredicate().getType());
		assertEquals(ResourceUtils.BASE_URI + "secret+identity", statement
				.getPredicate().getValue());
	}

	@Test
	public void predicateWithUrl() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("http://example.com/identity");

		Statement statement = new Statement(subject, predicate, "Clark");

		assertEquals(StatementItemType.IRI, statement.getPredicate().getType());
		assertEquals("http://example.com/identity", statement.getPredicate()
				.getValue());
	}

	@Test
	public void stringObject() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("foo");

		Statement statement = new Statement(subject, predicate, "Clark Kent");

		assertEquals(StatementItemType.literal, statement.getObject().getType());
		assertEquals("http://www.w3.org/2001/XMLSchema#string", statement
				.getObject().getDatatype());
		assertEquals("Clark Kent", statement.getObject().getValue());
	}

	@Test
	public void subjectWithoutUri() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("Superman");
		when(predicate.value()).thenReturn("foo");

		Statement statement = new Statement(subject, predicate, "Clark");
		assertEquals(StatementItemType.IRI, statement.getSubject().getType());
		assertEquals(ResourceUtils.BASE_URI + "Superman", statement
				.getSubject().getValue());
	}

	@Test
	public void subjectWithSpaces() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("The Green Lantern");
		when(predicate.value()).thenReturn("foo");

		Statement statement = new Statement(subject, predicate, "Alan Scott");
		assertEquals(StatementItemType.IRI, statement.getSubject().getType());
		assertEquals(ResourceUtils.BASE_URI + "The+Green+Lantern", statement
				.getSubject().getValue());
	}

	@Test
	public void subjectWithUri() throws Exception {
		Subject subject = mock(Subject.class);
		Predicate predicate = mock(Predicate.class);

		when(subject.value()).thenReturn("http://example.com/Superman");
		when(predicate.value()).thenReturn("foo");

		Statement statement = new Statement(subject, predicate, "Clark");
		assertEquals(StatementItemType.IRI, statement.getSubject().getType());
		assertEquals("http://example.com/Superman", statement.getSubject()
				.getValue());
	}
}
