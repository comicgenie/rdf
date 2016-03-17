package org.comicwiki.model.notes;

import java.util.Collection;

import org.comicwiki.Add;
import org.comicwiki.IRI;
import org.comicwiki.model.schema.Intangible;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.ObjectString;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ReprintNote", isBlankNode = true)
public class ReprintNote extends Intangible {

	@Predicate("note")
	@ObjectString
	public String[] note;

	@Predicate("print")
	@ObjectIRI
	public IRI firstPrint;
	
	@Predicate("reprint")
	@ObjectIRI
	public IRI reprint;
	
	public void addReprintNote(String n) {
		note = Add.one(note, n);
	}
	
	public void addReprintNote(Collection<String> n) {
		note = Add.both(note, n, String.class);
	}
	
	public void addReprintNote(String[] n) {
		note = Add.both(note, n, String.class);
	}
}
