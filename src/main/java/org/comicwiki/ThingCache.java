package org.comicwiki;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.comicwiki.model.schema.Thing;

import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;

public final class ThingCache {

	private static final KeyIDGenerator instanceIDGen = new KeyIDGenerator(0);
	
	private static final KeyIDGenerator resourceIDGen = new KeyIDGenerator(0);
	
	private static final HashMap<String, Thing> INSTANCE_CACHE = new HashMap<>(
			1000000);

	private static final HashBiMap<String, String> CPK_RESOURCE_MAP = HashBiMap
			.create();

	private static final HashMap<String, String> INSTANCE_RESOURCE_MAP = new HashMap<>(
			1000000);

	public static void add(Thing thing) {
		assignInstanceId(thing);
		INSTANCE_CACHE.put(thing.instanceId, thing);
	}
	
	private static String generateResourceId() {
		return "@" + resourceIDGen.createID();
	}
	
	private static String assignInstanceId(Thing thing) {
		if (Strings.isNullOrEmpty(thing.instanceId)) {
			thing.instanceId = "-" + instanceIDGen.createInstanceId();
		}
		return thing.instanceId;
	}

	public static synchronized void assignResourceIDs() {
		HashMap<String, String> instanceCpkMap = new HashMap<>(1000000);
		for (Thing thing : INSTANCE_CACHE.values()) {
			thing.compositePropertyKey = KeyUtils
					.readCompositePropertyKey(thing);
			instanceCpkMap.put(thing.instanceId, thing.compositePropertyKey);

			if (!CPK_RESOURCE_MAP.containsKey(thing.compositePropertyKey)) {
				thing.resourceId = generateResourceId();
				CPK_RESOURCE_MAP.put(thing.compositePropertyKey,
						thing.resourceId);
			} else {
				thing.resourceId = CPK_RESOURCE_MAP
						.get(thing.compositePropertyKey);
			}

			INSTANCE_RESOURCE_MAP.put(thing.instanceId, thing.resourceId);
		}
		instanceCpkMap.clear();

		
		for (Thing thing : INSTANCE_CACHE.values()) {

		}
	}

	public static void loadResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
	}
	
	public static void exportResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
	}
}
