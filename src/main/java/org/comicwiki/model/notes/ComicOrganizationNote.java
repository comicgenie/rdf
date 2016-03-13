package org.comicwiki.model.notes;

import org.comicwiki.IRI;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ComicOrganizationNote", isBlankNode = true)
public class ComicOrganizationNote extends StoryNote {

	@Predicate("comicOrganization")
	@ObjectIRI
	public IRI comicOrganization;
}
