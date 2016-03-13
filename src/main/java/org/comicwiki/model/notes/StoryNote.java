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
@Subject(value = "StoryNote", isBlankNode = true)
public class StoryNote extends Intangible {

	@Predicate("note")
	@ObjectString
	public Collection<String> note = new HashSet<>(3);

	@Predicate("story")
	@ObjectIRI
	public IRI story;
}
