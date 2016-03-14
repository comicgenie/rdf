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
@Subject(value = "StoryNote", isBlankNode = true)
public class StoryNote extends Intangible {

	@Predicate("note")
	@ObjectString
	public String[] note;

	@Predicate("story")
	@ObjectIRI
	public IRI story;
	
	public void addNote(String n) {
		note = Add.one(note, n);
	}
	
	public void addNote(String[] n) {
		note = Add.both(note, n, String.class);
	}
}
