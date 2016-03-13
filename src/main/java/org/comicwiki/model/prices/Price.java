package org.comicwiki.model.prices;

import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "Price", isBlankNode=true)
public class Price extends Thing {
	
	@Predicate("country")
	@ObjectString
	public String country;
	
	@Predicate("currency")
	@ObjectString
	public String currency;
	
	@Predicate("display")
	@ObjectString
	public String display;
	
	@Predicate("isInferred")
	@ObjectBoolean
	public Boolean isInferred;
	
	@Predicate("note")
	@ObjectString
	public String note;
}
