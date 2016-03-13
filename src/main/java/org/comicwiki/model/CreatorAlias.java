package org.comicwiki.model;

import org.comicwiki.IRI;
import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
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
@Subject(value = "CreatorAlias", isBlankNode = true)
public class CreatorAlias extends Intangible {
	
	@Predicate("alias")
	@ObjectIRI
	public IRI alias;
	
	@Predicate("creator")
	@ObjectIRI
	public IRI creator;
	
	@Predicate("issue")
	@ObjectIRI
	public IRI issue;
	
	@Predicate("role")
	@ObjectString
	public CreatorRole role;
	
	@Predicate("story")
	@ObjectIRI
	public IRI story;
	
}
