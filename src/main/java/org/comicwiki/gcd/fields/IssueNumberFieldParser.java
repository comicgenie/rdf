package org.comicwiki.gcd.fields;

import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.model.ComicIssueNumber;


public class IssueNumberFieldParser implements FieldParser<ComicIssueNumber>{

	private ThingFactory thingFactory;

	public IssueNumberFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}
	
	@Override
	public ComicIssueNumber parse(int field, Row row) {
		// TODO Auto-generated method stub
		return null;
	}

}
