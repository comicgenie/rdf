package org.comicwiki.gcd.fields;

import java.util.Collection;
import java.util.HashSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.spark.sql.Row;
import org.comicwiki.FieldParser;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.parser.PriceLexer;
import org.comicwiki.gcd.parser.PriceParser;
import org.comicwiki.gcd.parser.PriceParser.FractionPriceContext;
import org.comicwiki.gcd.parser.PriceParser.InferredPriceContext;
import org.comicwiki.gcd.parser.PriceParser.PriceContext;
import org.comicwiki.gcd.parser.PriceParser.PriceFieldContext;
import org.comicwiki.model.prices.BritishPrice;
import org.comicwiki.model.prices.DecimalPrice;
import org.comicwiki.model.prices.FractionPrice;
import org.comicwiki.model.prices.Price;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class PriceFieldParser extends BaseFieldParser implements
		FieldParser<Collection<Price>> {

	private static PriceFieldContext getContextOf(String textField,
			boolean failOnError) {
		PriceLexer lexer = new PriceLexer(new ANTLRInputStream(textField));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PriceParser parser = new PriceParser(tokens);
		if (failOnError) {
			// parser.setErrorHandler(new BailErrorStrategy());
		}
		return parser.priceField();
	}

	private Collection<Price> prices = new HashSet<>(3);

	private final ThingFactory thingFactory;

	protected PriceFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}

	private void addFractions(FractionPriceContext priceContext,
			FractionPrice price) {
		String priceFraction = getValue(priceContext.FRACTION());
		if (!Strings.isNullOrEmpty(priceFraction)) {
			String[] tokens = priceFraction.split("/");
			if (tokens.length == 2) {
				price.numerator = Integer.parseInt(tokens[0]);
				price.denominator = Integer.parseInt(tokens[1]);
			}
		}
	}

	private void fractionPrice(FractionPriceContext priceContext, String note) {
		if (priceContext != null) {
			if (priceContext.BRITISH() != null) {
				BritishPrice price = thingFactory.create(BritishPrice.class);
				price.display = priceContext.getText();
				addFractions(priceContext, price);
				String[] britishValue = getValue(priceContext.BRITISH()).split(
						"-");
				price.pounds = Integer.valueOf(britishValue[0]);
				price.shillings = Integer.valueOf(britishValue[1]);
				price.pence = Integer.valueOf(britishValue[2]);
				price.note = note;
				prices.add(price);
			} else {
				FractionPrice price = thingFactory.create(FractionPrice.class);
				price.display = priceContext.getText();
				addFractions(priceContext, price);
				price.note = note;
				prices.add(price);
			}
		}
	}

	private void inferredPrice(InferredPriceContext priceContext, String note) {
		if (priceContext != null) {
			price(priceContext.price(), true, note);
		}
	}

	@Override
	public Collection<Price> parse(int field, Row row) {
		HashSet<String> p = Sets.newHashSet(Splitter.on(';').trimResults()
				.omitEmptyStrings().split(row.getString(field)));
		for (String price : p) {
			PriceFieldContext pfc = getContextOf(price, false);
			String note = getValue(pfc.NOTE());
			price(pfc.price(), false, note);
			fractionPrice(pfc.fractionPrice(), note);
			inferredPrice(pfc.inferredPrice(), note);			
		}
		return prices;
	}

	private void price(PriceContext priceContext, boolean isInferred, String note) {
		if (priceContext != null) {
			DecimalPrice price = thingFactory.create(DecimalPrice.class);
			price.display = priceContext.getText();
			if (priceContext.PRICE() != null) {
				price.amount = Double.valueOf(priceContext.PRICE().getText());
			}
			price.currency = getValue(priceContext.CURRENCY_CODE());
			price.country = getValue(priceContext.COUNTRY());
			if (isInferred) {
				price.isInferred = true;
			}
			price.note = note;
			prices.add(price);
		}
	}

	@Override
	public Collection<Price> parse(String fieldValue) {
		return null;//noop
	}

}
