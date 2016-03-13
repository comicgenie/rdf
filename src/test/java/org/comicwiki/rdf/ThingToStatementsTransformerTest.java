package org.comicwiki.rdf;

import static org.comicwiki.rdf.DataType.XSD_ANYURI;
import static org.comicwiki.rdf.DataType.XSD_BOOLEAN;
import static org.comicwiki.rdf.DataType.XSD_GDAY;
import static org.comicwiki.rdf.DataType.XSD_LONG;
import static org.comicwiki.rdf.DataType.XSD_NONNEGATIVEINTEGER;
import static org.comicwiki.rdf.RdfFactory.BASE_URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.FieldWrapper;
import org.comicwiki.IRI;
import org.comicwiki.model.IdentityType;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectEnum;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ObjectURL;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.ParentClass;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;
import org.junit.Test;

public class ThingToStatementsTransformerTest {

	@Subject(value = "AClass", key = "name")
	private class AClass extends Person {

		public AClass() {
			this.resourceId = new IRI("@N123");
		}
	}

	@Subject(value = "BClass", key = "name")
	private class BClass extends Thing {

		@Predicate("myString")
		@ObjectString
		public int myString;

		public BClass() {
			this.resourceId = new IRI("@N123");
		}
	}

	@Subject(value = "DClass", key = "name")
	private class DClass extends Thing {

		@ParentClass
		public Person thing;

		public DClass() {
			this.resourceId = new IRI("@N123");
		}
	}
	
	@Subject(value = "EnumClass", key = "name")
	private class EnumClass extends Thing {

		public EnumClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("identity")
		@ObjectEnum
		@SchemaComicWiki
		public IdentityType identity = IdentityType.PUBLIC;
	}
	
	@Subject(value = "EnumClass", key = "name")
	private class EnumCollectionClass extends Thing {

		public EnumCollectionClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("identity")
		@ObjectEnum
		@SchemaComicWiki
		public Collection<IdentityType> identity = new HashSet<>();
	}
	
	@Subject(value = "BooleanClass", key = "name")
	private class BooleanClass extends Thing {

		public BooleanClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("hasIdentity")
		@ObjectBoolean
		@SchemaComicWiki
		public Boolean hasIdentity = true;
	}
	
	@Subject(value = "NumberClass", key = "name")
	private class NumberClass extends Thing {

		public NumberClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("count")
		@ObjectNumber
		@SchemaComicWiki
		public long count = 100L;
	}
	
	@Subject(value = "NumberClass", key = "name")
	private class NonNegativeClass extends Thing {

		public NonNegativeClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("count")
		@ObjectNonNegativeInteger
		@SchemaComicWiki
		public int count = 100;
	}
	
	@Subject(value = "XSDClass", key = "name")
	private class XSDClass extends Thing {

		public XSDClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("day")
		@ObjectXSD(DataType.XSD_GDAY)
		@SchemaComicWiki
		public Integer day = 16;
	}
	
	
	@Subject(value = "NumberClass", key = "name")
	private class URLClass extends Thing {

		public URLClass() {
			this.resourceId = new IRI("@N123");
		}
		
		@Predicate("site")
		@ObjectURL
		@SchemaComicWiki
		public URL site;
	}

	private static class NoSubjectClass extends Thing {
		protected IRI resourceId = new IRI("@N123");
	}

	@Test
	public void iriCollection() throws Exception {
		AClass c = new AClass();
		c.addColleague(new IRI(BASE_URI + "N1000"));

		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				"http://schema.org/colleague")), new RdfObject(BASE_URI
				+ "N1000", NodeType.IRI, null));
		System.out.println(statements);
		System.out.println(s1);
		assertTrue(statements.contains(s1));
	}

	@Test
	public void nonPredicateReturnsEmptyCollection() throws Exception {
		FieldWrapper mockedField = mock(FieldWrapper.class);
		when(mockedField.getAnnotation(Predicate.class)).thenReturn(null);
		assertEquals(
				0,
				ThingToStatementsTransformer.createStatements(null,
						mockedField, null).size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void noResourceID() throws Exception {
		Thing thing = new Thing();
		ThingToStatementsTransformer.transform(thing);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noSubjectAnnotation() throws Exception {
		ThingToStatementsTransformer.transform(new NoSubjectClass());
	}
	
	@Test(expected = NullPointerException.class)
	public void nullThing() throws Exception {
		ThingToStatementsTransformer.transform(null);
	}

	@Test
	public void parentClass() throws Exception {
		DClass c = new DClass();
		c.thing = new Person();
		c.thing.familyName = "Smith";
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				"http://schema.org/familyName")), new RdfObject(
				"Smith", NodeType.literal, DataType.XSD_STRING));
		assertTrue(statements.contains(s1));
	}
	
	@Test
	public void enumType() throws Exception {
		EnumClass c = new EnumClass();
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "identity")), new RdfObject(
				"PUBLIC", NodeType.literal, DataType.XSD_STRING));
		assertTrue(statements.contains(s1));
	}
	
	@Test
	public void booleanType() throws Exception {
		BooleanClass c = new BooleanClass();
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "hasIdentity")), true);
		assertTrue(statements.contains(s1));
		assertEquals(XSD_BOOLEAN, s1.getObject().getDatatype());
	}
	
	@Test
	public void number() throws Exception {
		NumberClass c = new NumberClass();
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "count")), 100L);
		assertTrue(statements.contains(s1));
		assertEquals(XSD_LONG, s1.getObject().getDatatype());
	}
	
	@Test
	public void url() throws Exception {
		URLClass c = new URLClass();
		c.site = new URL("http://comicwiki.org");
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "site")), new URL("http://comicwiki.org"));
		assertTrue(statements.contains(s1));
		assertEquals(XSD_ANYURI, s1.getObject().getDatatype());
	}
	
	@Test
	public void nonNegative() throws Exception {
		NonNegativeClass c = new NonNegativeClass();
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		ObjectNonNegativeInteger a = mock(ObjectNonNegativeInteger.class);
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "count")), a, 100);
		assertTrue(statements.contains(s1));
		assertEquals(XSD_NONNEGATIVEINTEGER, s1.getObject().getDatatype());
	}
	
	@Test
	public void xsdType() throws Exception {
		XSDClass c = new XSDClass();
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		ObjectXSD a = mock(ObjectXSD.class);
		when(a.value()).thenReturn(DataType.XSD_GDAY);
		
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "day")), a, 16);

		assertTrue(statements.contains(s1));
		
		assertEquals(XSD_GDAY, s1.getObject().getDatatype());
	}
	
	@Test
	public void enumCollection() throws Exception {
		EnumCollectionClass c = new EnumCollectionClass();
		c.identity.add(IdentityType.PUBLIC);
		c.identity.add(IdentityType.SECRET);
		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(4, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "identity")), new RdfObject(
				"PUBLIC", NodeType.literal, DataType.XSD_STRING));
		Statement s2 = new Statement(subject, new RdfPredicate(new IRI(
				BASE_URI + "identity")), new RdfObject(
				"SECRET", NodeType.literal, DataType.XSD_STRING));
		statements.contains(s1);
		statements.contains(s2);
	}

	@Test
	public void predicateClass() throws Exception {
		AClass c = new AClass();
		c.name = "AName";

		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(3, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				DataType.RDF_TYPE)), new RdfObject(BASE_URI + "AClass",
				NodeType.IRI, null));
		Statement s2 = new Statement(subject, new RdfPredicate(new IRI(BASE_URI
				+ "resourceId")), new RdfObject(RdfFactory.BASE_URI + "N123",
				NodeType.IRI, null));
		Statement s3 = new Statement(subject, new RdfPredicate(new IRI(
				"http://schema.org/name")), new RdfObject("AName",
				NodeType.literal, DataType.XSD_STRING));

		assertTrue(statements.contains(s1));
		assertTrue(statements.contains(s2));
		assertTrue(statements.contains(s3));
	}

	@Test
	public void stringCollection() throws Exception {
		AClass c = new AClass();
		c.addAlternateName("AnotherName");
		c.addAlternateName("AnotherName2");

		Collection<Statement> statements = ThingToStatementsTransformer
				.transform(c);
		assertEquals(4, statements.size());

		RdfSubject subject = new RdfSubject(new IRI(BASE_URI + "N123"));
		Statement s1 = new Statement(subject, new RdfPredicate(new IRI(
				"http://schema.org/alternateName")), new RdfObject(
				"AnotherName", NodeType.literal, DataType.XSD_STRING));
		Statement s2 = new Statement(subject, new RdfPredicate(new IRI(
				"http://schema.org/alternateName")), new RdfObject(
				"AnotherName2", NodeType.literal, DataType.XSD_STRING));

		System.out.println(statements);
		System.out.println(s1);
		System.out.println(s2);
		assertTrue(statements.contains(s1));
		assertTrue(statements.contains(s2));
	}	
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongType() throws Exception {
		BClass c = new BClass();
		ThingToStatementsTransformer.transform(c);
	}

}
