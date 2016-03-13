package org.comicwiki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.comicwiki.model.schema.Thing;

public class HashMapShard {

	public HashMap<IRI, Thing>[] maps;

	public HashMapShard(int shards) {
		maps = new HashMap[shards];
		for (int i = 0; i < shards; i++) {
			maps[i] = new HashMap<>(Integer.MAX_VALUE);
		}
	}

	public void put(IRI iri, Thing thing) {
		for (int i = 0; i < maps.length; i++) {
			if (maps[i].size() < Integer.MAX_VALUE - 1) {
				maps[i].put(iri, thing);
				break;
			}
		}
	}

	public Thing get(IRI iri) {
		for (int i = 0; i < maps.length; i++) {
			if (maps[i].containsKey(iri)) {
				return maps[i].get(iri);
			}
		}
		return null;
	}

	public Collection<Thing> values() {
		ArrayList<Thing> things = new ArrayList<Thing>();
		for (int i = 0; i < maps.length; i++) {
			things.addAll(maps[i].values());
		}
		return things;
	}
	
	public int size() {
		return maps[0].size();//TODO: add
	}
}
