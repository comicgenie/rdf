package org.comicwiki.model.schema;

import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@Subject(value = "http://schema.org/Language", key = "name")
public class Language extends Intangible {
	
	@SchemaComicWiki
	@Predicate("languageCode")
	@ObjectString
	public String languageCode;
}
