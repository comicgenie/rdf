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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.Statement;
import org.comicwiki.rdf.ThingToStatementsTransformer;
import org.comicwiki.rdf.TurtleImporter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFDatasetUtils;

public class Repository<T extends Thing> {

	private static final Logger LOG = Logger.getLogger("Repository");

	private final String name;

	public Repository(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

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
				} else if (targetValue instanceof Thing) {// CreativeWork
					mergeObjects(sourceValue, targetValue);
				} else if (targetValue instanceof IRI[]) {
					IRI[] tV = (IRI[]) targetValue;
					IRI[] sV = (IRI[]) sourceValue;
					field.set(target, Add.both(tV, sV, IRI.class));
				} else if (targetValue instanceof String[]) {
					String[] tV = (String[]) targetValue;
					String[] sV = (String[]) sourceValue;
					field.set(target, Add.both(sV, tV, String.class));
				} else if (targetValue instanceof Number[]) {
					Number[] tV = (Number[]) targetValue;
					Number[] sV = (Number[]) sourceValue;
					field.set(target, Add.both(tV, sV, Number.class));
				} else if (targetValue instanceof URL[]) {
					URL[] tV = (URL[]) targetValue;
					URL[] sV = (URL[]) sourceValue;
					field.set(target, Add.both(tV, sV, URL.class));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	// resourceId->Thing
	public final HashMap<IRI, T> cache = new HashMap<>(1000000);

	protected ObjectMapper mapper = new ObjectMapper();

	private ArrayList<RepositoryTransform> transforms = new ArrayList<>();

	public void add(Collection<T> items) {
		for (T item : items) {
			add(item);
		}
	}

	public final void add(T item) {
		IRI key = item.resourceId;
		T it = getByKey(key);
		if (it != null) {
			merge(item, it);
		} else {
			cache.put(key, item);
		}
	}

	public final void addTransform(RepositoryTransform transform) {
		transforms.add(transform);
	}

	public final void clear() {
		cache.clear();
		transforms.clear();
	}

	private T getByKey(IRI key) {
		return cache.get(key);
	}

	public void load(File file, DataFormat format) throws IOException,
			JsonLdError {
		load(new FileInputStream(file), format);
	}

	public void load(InputStream is, DataFormat format) throws IOException,
			JsonLdError {
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
			cache.put(t.instanceId, t);
		}
	}

	public <T> T merge(T source, T target) {
		mergeObjects(source, target);
		return target;
	}

	public void print() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, cache.values());
	}

	public void save(File file, DataFormat format) throws Exception {
		save(new FileOutputStream(file), format);
	}

	public void save(OutputStream out, DataFormat format) throws Exception {
		if (DataFormat.JSON.equals(format)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_DEFAULT);
			mapper.writeValue(out, cache.values());
		} else if (DataFormat.N_TRIPLES.equals(format)) {
			RDFDataset dataset = new RDFDataset();
			for (T t : cache.values()) {
				Collection<Statement> statements = ThingToStatementsTransformer
						.transform(t);

				for (Statement statement : statements) {
					dataset.addTriple(statement.getSubject().getValue(),
							statement.getPredicate().getValue(), statement
									.getObject().getValue());
				}
			}
			out.write(RDFDatasetUtils.toNQuads(dataset).getBytes());
		} else if (DataFormat.TURTLE.equals(format)) {
			RDFDataset dataset = new RDFDataset();
			for (T t : cache.values()) {
				Collection<Statement> statements = ThingToStatementsTransformer
						.transform(t);

				for (Statement statement : statements) {
					dataset.addTriple(statement.getSubject().getValue(),
							statement.getPredicate().getValue(), statement
									.getObject().getValue());
				}
			}
			Object output = JsonLdProcessor.fromRDF(RDFDatasetUtils
					.toNQuads(dataset));

			out.write(output.toString().getBytes());
		}
		out.close();
	}

	public final void transform() throws IOException {
		for (RepositoryTransform transform : transforms) {
			try {
				transform.transform();
			} catch (Exception e) {
				LOG.severe("Repository transform failure: " + name + ", "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
