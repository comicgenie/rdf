package org.comicwiki.rdf;

import java.util.Collection;

import org.comicwiki.IRI;
import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class ThingToStatementsTransformerTest {

	@Test(expected = NullPointerException.class)
	public void nullThing() throws Exception {
		ThingToStatementsTransformer.transform(null);
	}
	
	@Test
	public void noSubjectAnnotation() throws Exception {
		Thing thing = new Thing();
		thing.resourceId = new IRI("@N123");
		Collection<Statement> statements = ThingToStatementsTransformer.transform(thing);
		for(Statement statement : statements) 
			System.out.println(statement);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void noResourceID() throws Exception {
		Thing thing = new Thing();
		ThingToStatementsTransformer.transform(thing);
	}
}
