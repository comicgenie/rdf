package org.comicwiki.rdf;

import static org.comicwiki.rdf.DataType.XSD_ANYURI;
import static org.comicwiki.rdf.DataType.XSD_BOOLEAN;
import static org.comicwiki.rdf.DataType.XSD_BYTE;
import static org.comicwiki.rdf.DataType.XSD_DOUBLE;
import static org.comicwiki.rdf.DataType.XSD_FLOAT;
import static org.comicwiki.rdf.DataType.XSD_INTEGER;
import static org.comicwiki.rdf.DataType.XSD_LONG;
import static org.comicwiki.rdf.DataType.XSD_SHORT;
import static org.comicwiki.rdf.DataType.XSD_STRING;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.comicwiki.IRI;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;
import org.junit.Test;

public class RdfFactoryTest {

	@Test
	public void booleanFalse() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(false);
		assertEquals("false", rdf.getValue());
		assertEquals(XSD_BOOLEAN, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void expandIRI() throws Exception {
		RdfPredicate predicate = RdfFactory.createRdfPredicate(new IRI("@N123"));	
		assertEquals(RdfFactory.BASE_URI + "N123", predicate.getValue());
	}
	
	//@Test(expected = IllegalArgumentException.class)
	public void readResourceIdWithNoAtSign() throws Exception {
		RdfFactory.createRdfPredicate(new IRI("I1"));
	}
	
	@Test
	public void testByte() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject((byte) 4);
		assertEquals("4", rdf.getValue());
		assertEquals(XSD_BYTE, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testDouble() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(1.11d);
		assertEquals("1.11", rdf.getValue());
		assertEquals(XSD_DOUBLE, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testFloat() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(1.11f);
		assertEquals("1.11", rdf.getValue());
		assertEquals(XSD_FLOAT, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testInteger() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(1);
		assertEquals("1", rdf.getValue());
		assertEquals(XSD_INTEGER, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testLong() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(1L);
		assertEquals("1", rdf.getValue());
		assertEquals(XSD_LONG, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testShort() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(new Short("1"));
		assertEquals("1", rdf.getValue());
		assertEquals(XSD_SHORT, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testString() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject("MyName");
		assertEquals("MyName", rdf.getValue());
		assertEquals(XSD_STRING, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void testURL() throws Exception {
		RdfObject rdf = RdfFactory.createRdfObject(new URL("http://comicwiki.org"));
		assertEquals("http://comicwiki.org", rdf.getValue());
		assertEquals(XSD_ANYURI, rdf.getDatatype());
		assertEquals(NodeType.literal, rdf.getType());
	}
	
	@Test
	public void urlOnPredicate() throws Exception {
		RdfPredicate predicate = RdfFactory.createRdfPredicate(new IRI("http://comicwiki.org/name"));	
		assertEquals("http://comicwiki.org/name", predicate.getValue());
	}
	
	@Test
	public void objectIRI() throws Exception {
		RdfObject object = RdfFactory.createRdfObject(new IRI("http://comicwiki.org/N123"));	
		assertEquals("http://comicwiki.org/N123", object.getValue());
	}
			
	@Test
	public void urlOnSubject() throws Exception {
		RdfSubject subject = RdfFactory.createRdfSubject(new IRI("http://comicwiki.org/N123"));	
		assertEquals("http://comicwiki.org/N123", subject.getValue());
	}
}
