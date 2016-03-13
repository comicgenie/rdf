package org.comicwiki.model.prices;

import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;


@SchemaComicWiki
@Subject(value = "BritishPrice", isBlankNode=true)
public class BritishPrice extends FractionPrice {

	public Integer pounds;
	
	public Integer shillings;
	
	public Integer pence;
}
