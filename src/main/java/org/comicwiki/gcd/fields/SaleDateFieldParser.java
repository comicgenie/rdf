package org.comicwiki.gcd.fields;

import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.model.Instant;

public class SaleDateFieldParser implements FieldParser<Instant> {

	private final ThingFactory thingFactory;

	protected SaleDateFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}
	
	@Override
	public Instant parse(int field, Row row) {
		return null;
	}

}
