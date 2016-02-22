package org.comicwiki.rdf;

import static org.comicwiki.rdf.DataType.XSD_ANYURI;
import static org.comicwiki.rdf.DataType.XSD_BOOLEAN;
import static org.comicwiki.rdf.DataType.XSD_BYTE;
import static org.comicwiki.rdf.DataType.XSD_DOUBLE;
import static org.comicwiki.rdf.DataType.XSD_FLOAT;
import static org.comicwiki.rdf.DataType.XSD_INTEGER;
import static org.comicwiki.rdf.DataType.XSD_LONG;
import static org.comicwiki.rdf.DataType.XSD_NONNEGATIVEINTEGER;
import static org.comicwiki.rdf.DataType.XSD_SHORT;
import static org.comicwiki.rdf.DataType.XSD_STRING;
import static org.comicwiki.rdf.NodeType.IRI;
import static org.comicwiki.rdf.NodeType.literal;

import java.net.URI;
import java.net.URL;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.values.RdfObject;
import org.comicwiki.rdf.values.RdfPredicate;
import org.comicwiki.rdf.values.RdfSubject;

public final class RdfFactory {

	public final static String BASE_URI = "http://comicwiki.org/resources/";
	
	public final static String SCHEMA_ORG = "http://schema.org/";

	public static RdfObject createRdfObject(IRI iri) {
		return new RdfObject(expandIri(iri).value, IRI, null);
	}

	protected static RdfObject createRdfObject(boolean value) {
		return new RdfObject(String.valueOf(value), literal, XSD_BOOLEAN);
	}

	protected static RdfObject createRdfObject(Number value) {
		return new RdfObject(value.toString(), literal, getNumberType(value));
	}

	protected static RdfObject createRdfObject(
			ObjectNonNegativeInteger nonNegInt, Integer value) {
		return new RdfObject(String.valueOf(value), literal,
				XSD_NONNEGATIVEINTEGER);
	}

	protected static RdfObject createRdfObject(ObjectXSD xsd, Object value) {
		if (DataType.XSD_GDAY.equals(xsd.value())) {
			return new RdfObject(String.valueOf(value), literal, xsd.value());
		}
		return null;
	}

	protected static RdfObject createRdfObject(String value) {
		return new RdfObject(value, literal, XSD_STRING);
	}

	protected static RdfObject createRdfObject(URL value) {
		return new RdfObject(value.toString(), literal, XSD_ANYURI);
	}

	protected static RdfObject createRdfObject(URI value) {
		return new RdfObject(value.toString(), literal, XSD_ANYURI);
	}

	public static RdfPredicate createRdfPredicate(IRI iri) {
		return new RdfPredicate(expandIri(iri));
	}

	public static RdfSubject createRdfSubject(IRI iri) {
		return new RdfSubject(expandIri(iri));
	}

	private static String getNumberType(Number value) {
		if (value instanceof Integer) {
			return XSD_INTEGER;
		} else if (value instanceof Long) {
			return XSD_LONG;
		} else if (value instanceof Float) {
			return XSD_FLOAT;
		} else if (value instanceof Double) {
			return XSD_DOUBLE;
		} else if (value instanceof Short) {
			return XSD_SHORT;
		} else if (value instanceof Byte) {
			return XSD_BYTE;
		}
		throw new IllegalArgumentException("Unrecognized number type: " + value);
	}

	private static IRI expandIri(IRI iri) {
		if (isAbsolute(iri.value)) {
			return iri;
		}
		if (iri.value.startsWith("@")) {
			iri.value = iri.value.replaceFirst("^@", BASE_URI);
			return iri;
		} 
		
		iri.value = BASE_URI + iri.value;
		return iri;
		//throw new IllegalArgumentException(
		//		"Illegal IRI. Must be an absolute URL or a @resourceId: "
		//				+ iri.value);

	}

	private static boolean isAbsolute(String iri) {
		return iri.startsWith("http");
	}
}
