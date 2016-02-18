package org.comicwiki;

import static org.junit.Assert.*;

import org.junit.Test;

public class IRITest {

	@Test
	public void create() throws Exception {
		IRICache iriCache = new IRICache();
		IRI iri = IRI.create("1", iriCache);
		IRI iri2 = IRI.create("1", iriCache);
		assertEquals(System.identityHashCode(iri), System.identityHashCode(iri2));
	}
	
	@Test
	public void equals() throws Exception {
		IRICache iriCache = new IRICache();
		IRI iri = IRI.create("1", iriCache);
		IRI iri2 = IRI.create("1", iriCache);
		assertTrue(iri.equals(iri2));
	}
}
