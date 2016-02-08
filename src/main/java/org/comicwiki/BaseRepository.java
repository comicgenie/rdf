/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.comicwiki.model.CreativeWorkExtension;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.Statement;
import org.comicwiki.rdf.StatementFactory;
import org.comicwiki.rdf.TurtleImporter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFDatasetUtils;

public abstract class BaseRepository<T extends Thing> {

	private static Object mergeObjects(Object source, Object target) {
		Field[] fields = source.getClass().getFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object sourceValue = field.get(source);
				if (sourceValue == null) {
					continue;
				}
				Object targetValue = field.get(target);
				if (targetValue == null) {
					field.set(target, sourceValue);
				} else if (targetValue instanceof Collection) {
					Collection cT = (Collection<?>) targetValue;
					Collection sT = (Collection<?>) sourceValue;
					cT.addAll(sT);
				} else if (targetValue instanceof Thing
						|| targetValue instanceof CreativeWorkExtension) {
					mergeObjects(sourceValue, targetValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	protected final HashMap<ComicKey, T> cache = new HashMap<>();

	ComicKeyRepository ckRepo = new ComicKeyRepository();

	protected ObjectMapper mapper = new ObjectMapper();

	public void add(Collection<T> items) {
		for (T item : items) {
			add(item);
		}
	}

	public void add(T item) {
		ComicKey key = KeyUtils.createComicKey(item);
		if (contains(key)) {
			merge(item, getByKey(key));
		} else {
			cache.put(key, item);
		}
	}

	public boolean contains(ComicKey key) {
		return cache.containsKey(key);
	}

	public void exportData(OutputStream out, DataFormat format)
			throws Exception {
		if (DataFormat.JSON.equals(format)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, cache.values());
		} else if (DataFormat.TURTLE.equals(format)) {
			RDFDataset dataset = new RDFDataset();
			for (T t : cache.values()) {
				Collection<Statement> statements = StatementFactory
						.transform(t);
				for (Statement statement : statements) {
					dataset.addTriple(statement.getSubject().getValue(),
							statement.getPredicate().getValue(), statement
									.getObject().getValue());
				}

			}
			RDFDatasetUtils.toNQuads(dataset);
		}
	}

	public Collection<T> find(Collection<ComicMatcher<T>> matchers) {
		if (matchers == null) {
			return cache.values();
		}

		Collection<T> items = new HashSet<T>();
		for (T item : cache.values()) {
			for (ComicMatcher<T> matcher : matchers) {
				if (!matcher.match(item)) {
					continue;
				}
				items.add(item);
			}
		}
		return items;
	}

	public T getByKey(ComicKey key) {
		return cache.get(key);
	}

	public void importData(InputStream is, DataFormat format)
			throws IOException, JsonLdError {
		Collection<T> results = new TreeSet<T>();
		if (DataFormat.JSON.equals(format)) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
			ObjectMapper mapper = new ObjectMapper();

			results = mapper.readValue(is, mapper.getTypeFactory()
					.constructCollectionType(Collection.class, clazz));
		} else if (DataFormat.TURTLE.equals(format)) {
			String data = FileUtils.readStream(is);
			results = TurtleImporter.importTurtle(data, new TreeSet<T>());

		} else if (DataFormat.N_TRIPLES.equals(format)) {
			String data = FileUtils.readStream(is);
			results = TurtleImporter.importNTriples(data, new TreeSet<T>());
		}

		for (T t : results) {
			cache.put(KeyUtils.createComicKey(t), t);
		}
	}

	public abstract void load() throws IOException;

	public void load(File file) throws IOException {
		RepositoryFile<T> repoFile = mapper.readValue(file,
				RepositoryFile.class);
		Collection<T> values = repoFile.values;
		for (T value : values) {
			add(value);
		}
	}

	public <T> T merge(T source, T target) {
		mergeObjects(source, target);
		/*
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		Field[] fields = clazz.getFields();
		*/
		/*
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object sourceValue = field.get(source);
				if (sourceValue == null) {
					continue;
				}
				Object targetValue = field.get(source);
				if (targetValue == null) {
					field.set(target, sourceValue);
				} else if (targetValue instanceof Collection) {
					Collection cT = (Collection<?>) targetValue;
					Collection sT = (Collection<?>) sourceValue;
					cT.addAll(sT);
				} else if (targetValue instanceof Thing
						|| targetValue instanceof CreativeWorkExtension) {
					mergeObjects(sourceValue, targetValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
		return target;
	}

//	public abstract T merge(T source, T target);

	public void print() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, cache.values());
	}

	public void save(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		RepositoryFile<T> repoFile = new RepositoryFile<T>();
		repoFile.values = cache.values();
		mapper.setSerializationInclusion(Include.NON_DEFAULT);
		mapper.writeValue(fos, repoFile);
		fos.close();
	}

}
