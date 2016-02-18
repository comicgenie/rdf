package org.comicwiki.model;

import org.comicwiki.IRI;
import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Relation between a creator and an alias used within a specific context (Story, Issue)
 */
@JsonInclude(Include.NON_DEFAULT)
@SchemaComicWiki
@Subject(value = "CreatorAlias", compositeKey= {"creator", "alias"})
public class CreatorAlias extends Intangible {
	
	@Predicate("creator")
	@ObjectIRI
	public IRI creator;
	
	@Predicate("alias")
	@ObjectIRI
	public IRI alias;
	
}
