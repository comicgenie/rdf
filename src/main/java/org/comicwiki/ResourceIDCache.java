package org.comicwiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceIDCache {

	public static class MaxPair {
		long a;

		long n;

		public MaxPair() {
		}

		public MaxPair(long n, long a) {
			this.n = n;
			this.a = a;
		}
	}

	protected static MaxPair getNumber(String resourceId) {
		boolean isIndividual = resourceId.startsWith("@N");
		boolean isAnonymous = resourceId.startsWith("@A");
		if (!isIndividual && !isAnonymous) {
			throw new IllegalArgumentException(
					"resourceId does not start with a '@N' or '@A'");
		}

		MaxPair maxPair = new MaxPair();
		long current = Long.parseLong(resourceId.substring(2,
				resourceId.length()));
		if (isIndividual) {
			maxPair.n = current;
		} else {
			maxPair.a = current;
		}
		return maxPair;

	}

	private KeyIDGenerator anonymousIDGen = new KeyIDGenerator(0);

	private HashMap<String, IRI> cpkResourceMap = new HashMap<>(100000);

	private KeyIDGenerator resourceIDGen = new KeyIDGenerator(0);

	@Inject
	public ResourceIDCache() {
	}

	public void clear() {
		cpkResourceMap.clear();
		cpkResourceMap = null;
	}
	public void exportResourceIDs(File file) throws IOException {
		//ObjectMapper mapper = new ObjectMapper();
		//FileOutputStream fos = new FileOutputStream(file);		
		//mapper.writeValue(fos, cpkResourceMap);
		//fos.close();
		
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(file, JsonEncoding.UTF8);
		
		generator.writeStartObject();
		generator.writeStartArray();
		for(Map.Entry<String, IRI> e : cpkResourceMap.entrySet()) {
			generator.writeStartObject();
			generator.writeStringField(e.getKey(), e.getValue().value); 
			generator.writeEndObject();
		}
		generator.writeEndArray();
		generator.writeEndObject();
		generator.close();
	}

	public String generateAnonymousId() {
		return "@A" + anonymousIDGen.createID();
	}

	public String generateResourceId() {
		return "@N" + resourceIDGen.createID();
	}

	public IRI get(String key) {
		return cpkResourceMap.get(key);
	}

	public void loadResourceIDs(File file) throws IOException {
		// CPK_RESOURCE_MAP
		setIndex();
	}

	public void put(String key, IRI value) {
		cpkResourceMap.put(key, value);
	}

	protected MaxPair setIndex() {
		long maxN = 0, maxA = 0;
		for (IRI resourceId : cpkResourceMap.values()) {
			if (resourceId == null || Strings.isNullOrEmpty(resourceId.value)) {
				throw new IllegalArgumentException("empty resourceId");
			}
			MaxPair pair = getNumber(resourceId.value);
			if (pair.a > maxA) {
				maxA = pair.a;
			}

			if (pair.n > maxN) {
				maxN = pair.n;
			}
		}
		resourceIDGen = new KeyIDGenerator(maxN);
		anonymousIDGen = new KeyIDGenerator(maxA);

		return new MaxPair(maxN, maxA);
	}
}
