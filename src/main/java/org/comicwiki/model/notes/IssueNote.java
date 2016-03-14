package org.comicwiki.model.notes;

import org.comicwiki.Add;
import org.comicwiki.IRI;
import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "IssueNote", isBlankNode = true)
public class IssueNote extends Intangible {

	@Predicate("note")
	@ObjectString
	public String[] note;

	@Predicate("issue")
	@ObjectIRI
	public IRI issue;
	
	public void addIssueNote(String n) {
		note = Add.one(note, n);
	}
}
