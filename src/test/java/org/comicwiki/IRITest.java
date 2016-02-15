package org.comicwiki;

import static org.junit.Assert.*;

import org.junit.Test;

public class IRITest {

	@Test
	public void create() throws Exception {
		IRI iri = IRI.create("1");
		IRI iri2 = IRI.create("1");
		assertEquals(System.identityHashCode(iri), System.identityHashCode(iri2));
	}
}
