package org.comicwiki;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.ComicUniverse;
import org.comicwiki.model.schema.ComicIssue;
import org.comicwiki.model.schema.ComicSeries;
import org.comicwiki.model.schema.ComicStory;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Person;

public class Repositories {

	public static Repository<ComicCharacter> COMIC_CHARACTERS = new Repository<>();

	public static Repository<Person> COMIC_CREATOR = new Repository<>();

	public static Repository<ComicIssue> COMIC_ISSUE = new Repository<>();

	public static Repository<ComicOrganization> COMIC_ORGANIZATIONS = new Repository<>();

	public static Repository<ComicSeries> COMIC_SERIES = new Repository<>();

	public static Repository<ComicStory> COMIC_STORIES = new Repository<>();

	public static Repository<ComicUniverse> COMIC_UNIVERSE = new Repository<>();

	public static Repository<Country> COUNTRY = new Repository<>();
}
