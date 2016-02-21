package org.comicwiki.model;

import java.util.Date;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectDateInterval;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "Interval", key = "name", isBlankNode = true)
public class Interval extends TemporalEntity {

	@Predicate("intervalBegin")
	@ObjectIRI
	public IRI begin;
	
	@Predicate("intervalEnd")
	@ObjectIRI
	public IRI end;
	
	@Predicate("intervalDate")
	@ObjectDateInterval
	public Date intervalDate;
}
