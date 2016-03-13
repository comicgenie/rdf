package org.comicwiki.model.prices;

import org.comicwiki.rdf.DataType;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "DecimalPrice", isBlankNode=true)
public class DecimalPrice extends Price {

	@Predicate("amount")
	@ObjectXSD(DataType.XSD_DECIMAL)
	public double amount;
}
