package org.comicwiki;

import java.util.Collection;
import java.util.HashMap;

import com.google.inject.Singleton;

@Singleton
public class IRICache {

	private HashMap<String, IRI> sIriMap = new HashMap<>(1000000);
	
	public void clear() {
		sIriMap.clear();
		sIriMap = null;
	}
	public Collection<IRI> values() {
		return sIriMap.values();
	}
	
	public IRI get(String iri) {
		return sIriMap.get(iri);
	}
	
	public void add(IRI iri) {
		sIriMap.put(iri.value, iri);
	}
	
	public IRI remove(String iri) {
		return sIriMap.remove(iri);
	}
}
