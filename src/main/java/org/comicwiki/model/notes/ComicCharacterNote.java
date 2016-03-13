package org.comicwiki.model.notes;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ComicCharacterNote", isBlankNode = true)
public class ComicCharacterNote extends StoryNote {
	@Predicate("comicCharacter")
	@ObjectIRI
	public IRI comicCharacter;
}
