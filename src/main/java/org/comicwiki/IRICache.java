package org.comicwiki;

import java.util.Collection;
import java.util.HashMap;

public class IRICache {

	private final HashMap<String, IRI> sIriMap = new HashMap<>(1000000);
	
	public Collection<IRI> values() {
		return sIriMap.values();
	}
	
	public boolean contains(String iri) {
		return sIriMap.containsKey(iri);
	}
	
	public IRI get(String iri) {
		return sIriMap.get(iri);
	}
	
	public void add(IRI iri) {
		sIriMap.put(iri.value, iri);
	}
}
