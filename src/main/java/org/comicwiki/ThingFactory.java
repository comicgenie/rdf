package org.comicwiki;

import org.comicwiki.model.schema.Thing;

public class ThingFactory {

	public static <T extends Thing> T create(Class<T> clazz) {
		Thing thing;
		try {
			thing = (Thing) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		ThingCache.add(thing);
		return (T) thing;
	}

}
