package org.comicwiki.model;


import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.DataType;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "Price", isBlankNode=true)
public class Price extends Thing {

	@Predicate("amount")
	@ObjectXSD(DataType.XSD_DECIMAL)
	public double amount;
	
	@Predicate("currency")
	@ObjectString
	public String currency;
	
	@Predicate("display")
	@ObjectString
	public String display;
}
