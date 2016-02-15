package org.comicwiki;

import java.util.Collection;
import java.util.HashMap;

public class IRICache {

	private static final HashMap<String, IRI> sIriMap = new HashMap<>(1000000);
	
	public static Collection<IRI> values() {
		return sIriMap.values();
	}
	
	public static boolean contains(String iri) {
		return sIriMap.containsKey(iri);
	}
	
	public static IRI get(String iri) {
		return sIriMap.get(iri);
	}
	
	public static void add(IRI iri) {
		sIriMap.put(iri.value, iri);
	}
}
