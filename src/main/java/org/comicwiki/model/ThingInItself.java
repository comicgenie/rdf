package org.comicwiki.model;

import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "ThingInItself", key = "name")
public class ThingInItself extends Thing {

}
