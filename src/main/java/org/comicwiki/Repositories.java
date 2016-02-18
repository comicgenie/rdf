package org.comicwiki;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicOrganization;
import org.comicwiki.model.ComicUniverse;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.comicwiki.model.schema.bib.ComicSeries;
import org.comicwiki.model.schema.bib.ComicStory;
import org.comicwiki.transforms.ComicCharacterTransform;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Repositories {

	private final HashMap<Class<? extends Thing>, Repository<? extends Thing>> sThingRepoMap = new HashMap<>();

	public final Repository<ComicCharacter> COMIC_CHARACTERS = new Repository<>();

	public final Repository<Person> COMIC_CREATOR = new Repository<>();

	public final Repository<ComicIssue> COMIC_ISSUE = new Repository<>();

	public final Repository<ComicOrganization> COMIC_ORGANIZATIONS = new Repository<>();

	public final Repository<ComicSeries> COMIC_SERIES = new Repository<>();

	public final Repository<ComicStory> COMIC_STORIES = new Repository<>();

	public final Repository<ComicUniverse> COMIC_UNIVERSE = new Repository<>();

	public final Repository<Country> COUNTRY = new Repository<>();

	public Collection<Repository<? extends Thing>> getRepositories() {
		return sThingRepoMap.values();
	}

	public <T extends Thing> Repository<Thing> getRepository(Class<T> clazz) {
		return (Repository<Thing>) sThingRepoMap.get(clazz);
	}

	@Inject
	public Repositories() {
		sThingRepoMap.put(Person.class, COMIC_CREATOR);
		sThingRepoMap.put(ComicIssue.class, COMIC_ISSUE);
		sThingRepoMap.put(ComicOrganization.class, COMIC_ORGANIZATIONS);
		sThingRepoMap.put(ComicUniverse.class, COMIC_UNIVERSE);
		sThingRepoMap.put(Country.class, COUNTRY);
		sThingRepoMap.put(ComicCharacter.class, COMIC_CHARACTERS);
		sThingRepoMap.put(ComicStory.class, COMIC_STORIES);

		PersonNameMatcher namesImporter = new PersonNameMatcher();
		try {
			namesImporter.load(new File(
					"./src/main/resources/names/yob2014.txt"));
			namesImporter.loadLastNames(new File(
					"./src/main/resources/names/lastname.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		COMIC_CHARACTERS
				.addTransform(new ComicCharacterTransform(namesImporter, this));
	}

}
