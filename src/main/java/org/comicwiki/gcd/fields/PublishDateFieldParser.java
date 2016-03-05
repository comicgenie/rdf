package org.comicwiki.gcd.fields;

import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.model.TemporalEntity;

public class PublishDateFieldParser implements FieldParser<TemporalEntity> {

	private ThingFactory thingFactory;

	protected PublishDateFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}
	
	@Override
	public TemporalEntity parse(int field, Row row) {
		return null;
	}

}
