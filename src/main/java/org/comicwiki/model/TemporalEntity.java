package org.comicwiki.model;

import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "TemporalEntity", key = "name")
public class TemporalEntity extends Intangible {
	
}
