package org.comicwiki;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.model.schema.Thing;

public class ComicKeyRepository {
	
	protected Collection<ComicKey> cache = new HashSet<>(1000000);
	
	public boolean containsKeyFor(Thing thing) {
		return contains(KeyUtils.createComicKey(thing));
	}
	
	public boolean contains(ComicKey key) {
		return cache.contains(key);
	}
	
	public void addKeyFor(Thing thing) {
		ComicKey comicKey = KeyUtils.createComicKey(thing);
		if(!contains(comicKey)) {
			cache.add(comicKey);
		}
	}	
}
