package org.comicwiki.model.prices;

import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "FractionPrice", isBlankNode=true)
public class FractionPrice extends Price {

	public Integer numerator;
	
	public Integer denominator;
	
}
