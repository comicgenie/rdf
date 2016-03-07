package org.comicwiki.gcd.fields;

import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.model.Instant;

import com.google.common.base.Strings;

public class SaleDateFieldParser implements FieldParser<Instant> {

	private final ThingFactory thingFactory;

	protected SaleDateFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}
	
	@Override
	public Instant parse(int field, Row row) {
		Instant instant = thingFactory.create(Instant.class);
		String date = row.getString(field);
		instant.label = date;
		try {
			
			String[] tokens = date.split("[-]");
			if(tokens.length > 0) {
				instant.year = Integer.parseInt(tokens[0]);
			} 
			if(tokens.length > 1) {
				instant.month = Integer.parseInt(tokens[1]);
			}
			if(tokens.length > 2) {
				instant.day = Integer.parseInt(tokens[2]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return instant;
	}

}
