package org.comicwiki.gcd.fields;

import java.util.Collection;

import org.apache.spark.sql.Row;
import org.comicwiki.FieldParser;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;


public class StringFieldParser implements FieldParser<Collection<String>>{

	private boolean toUpperCase;

	public StringFieldParser(boolean toUpperCase) {
		this.toUpperCase = toUpperCase;
	}
	
	@Override
	public Collection<String> parse(int field, Row row) {
		String s = row.getString(field);
		if(toUpperCase) {
			s = s.toUpperCase();
		}
		return Sets.newHashSet(Splitter.on(';').trimResults()
				.omitEmptyStrings().split(s));
	}

	@Override
	public Collection<String> parse(String fieldValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
