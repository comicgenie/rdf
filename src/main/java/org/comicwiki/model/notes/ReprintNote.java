package org.comicwiki.model.notes;

import java.util.Collection;
import java.util.HashSet;

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
	public Collection<String> note = new HashSet<>(3);

	@Predicate("print")
	@ObjectIRI
	public IRI firstPrint;
	
	@Predicate("reprint")
	@ObjectIRI
	public IRI reprint;
	
	public void addReprintNote(String n) {
		if(note == null) {
			note = new HashSet<>(3); 
		}
		note.add(n);
	}
	
	public void addReprintNote(Collection<String> n) {
		if(note == null) {
			note = new HashSet<>(3); 
		}
		note.addAll(n);
	}
}
