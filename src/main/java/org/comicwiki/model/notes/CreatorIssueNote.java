package org.comicwiki.model.notes;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectBoolean;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "CreatorIssueNote", isBlankNode = true)
public class CreatorIssueNote extends IssueNote {

	@Predicate("creator")
	@ObjectIRI
	public IRI creator;
	
	@Predicate("alias")
	@ObjectIRI
	public IRI alias;
	
	@Predicate("isCreatorUncertain")
	@ObjectBoolean
	public Boolean isCreatorUncertain;
}
