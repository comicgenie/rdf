package org.comicwiki;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;

public class ResourceIDCache {

	protected static long getNumber(String resourceId) {
		if(!resourceId.startsWith("@I")) {
			throw new IllegalArgumentException("resourceId does not start with a '@I'");
		}
		return Long.parseLong(resourceId.substring(2, resourceId.length()));
	}

	private final HashMap<String, String> cpkResourceMap = new HashMap<>();

	private KeyIDGenerator resourceIDGen = new KeyIDGenerator(0);

	@Inject
	public ResourceIDCache() {
	}

	public boolean containsKey(String key) {
		return cpkResourceMap.containsKey(key);
	}

	public void exportResourceIDs(File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, cpkResourceMap);
		// CPK_RESOURCE_MAP
	}

	public String generateResourceId() {
		return "@" + resourceIDGen.createID();
	}

	public String get(String key) {
		return cpkResourceMap.get(key);
	}

	public void loadResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
		setIndex();
	}

	public void put(String key, String value) {
		cpkResourceMap.put(key, value);
	}

	protected long setIndex() {
		long max = 0, current = 0;
		for (String resourceId : cpkResourceMap.values()) {
			if (Strings.isNullOrEmpty(resourceId)) {
				throw new IllegalArgumentException("empty resourceId");
			}
			current = getNumber(resourceId);
			if (current > max) {
				max = current;
			}

		}
		resourceIDGen = new KeyIDGenerator(max);
		return max;
	}
}
