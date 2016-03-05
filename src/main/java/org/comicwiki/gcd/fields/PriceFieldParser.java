package org.comicwiki.gcd.fields;

import java.util.Collection;
import java.util.HashSet;

import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.model.Price;

public class PriceFieldParser implements FieldParser<Collection<Price>>{

	private final ThingFactory thingFactory;

	protected PriceFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}
	
	@Override
	public Collection<Price> parse(int field, Row row) {
		/*
		issueRow.price = parseField(
				Columns.PRICE,
				row,
				(f, r) -> {
					return Sets.newHashSet(Splitter.on(';').trimResults()
							.omitEmptyStrings().split(r.getString(f)));
				});
		*/
		Collection<Price> prices = new HashSet<>(3);
		//TODO: implement
		return prices;
	}

}
