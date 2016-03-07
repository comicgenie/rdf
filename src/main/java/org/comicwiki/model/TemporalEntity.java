package org.comicwiki.model;

import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "TemporalEntity", isBlankNode = true)
public class TemporalEntity extends Intangible {
	
	public String label;
}
