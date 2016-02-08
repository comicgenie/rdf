package org.comicwiki.rdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.comicwiki.model.schema.Thing;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.impl.NQuadRDFParser;
import com.github.jsonldjava.impl.TurtleRDFParser;
import com.github.jsonldjava.utils.JsonUtils;

public class TurtleImporter {

	public static <T extends Thing> Collection<T> importTurtle(String data,
			Collection<T> output) throws JsonLdError, JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		return importFromDataset(new TurtleRDFParser().parse(data), output);
	}

	private static <T extends Thing> Collection<T> importFromDataset(
			RDFDataset dataset, Collection<T> output) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Statement> statements = new ArrayList<>();
		for (Entry<String, Object> entry : dataset.entrySet()) {
			for (Object o : (ArrayList) entry.getValue()) {
				Statement statement = mapper.readValue(JsonUtils.toString(o),
						Statement.class);
				statements.add(statement);
			}
		}
		for (Statement statement : statements) {
			// Based on Subject, create object of type T
			// Go through annotations of type T and match IRI, fill in values
		}

		return output;
	}

	public static <T extends Thing> Collection<T> importNTriples(String data,
			Collection<T> output) throws JsonLdError, JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		return importFromDataset(new NQuadRDFParser().parse(data), output);
	}
}
