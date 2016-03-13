package org.comicwiki.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.comicwiki.DataFormat;
import org.comicwiki.IRI;
import org.comicwiki.IRICache;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.ComicCreativeWork;
import org.comicwiki.transforms.ComicCharacterTransform;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jsonldjava.utils.JsonUtils;

public class ComicCharacterRepositoryTest {

	private static PersonNameMatcher namesImporter;

	@After
	public void clean() {
		new Repositories(new PersonNameMatcher()).COMIC_CHARACTERS.clear();
	}

	@Test
	public void testMergeCollectionField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.addAlternateName("A1");
		ComicCharacter c2 = new ComicCharacter();
		c2.addAlternateName("A2");

		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(2, c2.alternateNames.size());
		assertTrue(c2.alternateNames.contains("A1"));
		assertTrue(c2.alternateNames.contains("A2"));

		new Repositories(new PersonNameMatcher()).COMIC_CHARACTERS.save(System.out,
				DataFormat.N_TRIPLES);
	}

	@Test
	public void testMergeCreativeField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new ComicCreativeWork();
		c1.creativeWork.addPublisher(IRI.create("pub1", new IRICache()));
		ComicCharacter c2 = new ComicCharacter();

		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(IRI.create("pub1", new IRICache()),
				c2.creativeWork.publishers.iterator().next());
	}

	@Test
	public void testMergeCreativeFieldWithCollection() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new ComicCreativeWork();
		c1.creativeWork.addArtist(IRI.create("ART1", new IRICache()));

		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.addArtist(IRI.create("ART2", new IRICache()));

		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(2, c2.creativeWork.artists.size());
		assertTrue(c2.creativeWork.artists.contains(IRI.create("ART2",
				new IRICache())));
	}

	@Test
	public void testNoMergeCreativeField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new ComicCreativeWork();
		c1.creativeWork.addPublisher(IRI.create("pub1", new IRICache()));
		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.addPublisher(IRI.create("pub2", new IRICache()));

		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.merge(c1, c2);
		c2.creativeWork.publishers.contains(IRI.create("pub2", new IRICache()));
		System.out.println(JsonUtils.toPrettyString(c2));
	}

	@Test
	public void testExport() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "X1";
		c1.addAlternateName("A1");

		ComicCharacter c2 = new ComicCharacter();
		c2.name = "X2";
		c2.addAlternateName("A2");

		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		repositories.COMIC_CHARACTERS.add(c2);
		repositories.COMIC_CHARACTERS.save(System.out, DataFormat.JSON);
	}

	/**
	 *
	 * @throws Exception
	 */

	@BeforeClass
	public static void oneTimeSetUp() {
		namesImporter = new PersonNameMatcher();
		try {
			namesImporter.load(new File(
					"./src/main/resources/names/yob2014.txt"));
			namesImporter.loadLastNames(new File(
					"./src/main/resources/names/lastname.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void maleFirstNameOnly() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Vaden";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Vaden", c1.givenName);
		assertNull(c1.familyName);
	}
	
	@Test
	public void maleFirstNameWithMalePrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Vaden";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Vaden", c1.givenName);
		assertNull(c1.familyName);
	}

	@Test
	public void femaleFirstNameOnly() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Alice";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertNull(c1.familyName);
	}
	
	@Test
	public void lastNameOnly() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Smith", c1.familyName);
	}
	
	@Test
	public void lastNameWithSuffix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Smith Esq.";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Smith", c1.familyName);
	}
	
	@Test
	public void femaleFirstNameWithPrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Ms. Alice";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Ms", c1.honorificPrefix);
		assertNull(c1.familyName);
	}
	
	@Test
	public void femaleFullNameWithPrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Ms. Alice Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("Ms", c1.honorificPrefix);
	}
	
	@Test
	public void maleFullNameWithPrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Jim Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Jim", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("Mr", c1.honorificPrefix);
	}
	
	@Test
	public void maleFullNameWithFemalePrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Ms. Jim Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Smith", c1.familyName);
		assertEquals("F", c1.gender);
		assertEquals("Ms", c1.honorificPrefix);
	}
	
	@Test
	public void fullMaleNameWithNoPrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Jim Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Jim", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("M", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullMaleNameWithNoPrefixAndMiddleInitial() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Jim T. Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Jim", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("M", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullNameWithNoPrefixAndMiddleInitial() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Foo T. Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Foo", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullMaleNameWithNoPrefixAndMiddleName() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Jim Tiberius Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Jim", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("M", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullFemaleNameWithNoPrefixAndFirstInitialAndMiddleName() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "B. Alice Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("F", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullMaleNameWithNoPrefixAndFirstInitialAndMiddleName() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "J. Jim Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Jim", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("M", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullFemaleNameWithNoPrefixAndMiddleInitial() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Alice J. Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("F", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullFemaleNameWithNoPrefixAndMiddleName() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Alice Janice Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("F", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	@Test
	public void fullFemaleNameWithNoPrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Alice Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Alice", c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("F", c1.gender);
		assertNull(c1.honorificPrefix);
	}
	
	
	
	
	@Test
	public void femaleFullNameWithMalePrefix() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Alice Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Smith", c1.familyName);
		assertEquals("M", c1.gender);
		assertEquals("Mr", c1.honorificPrefix);
	}
	
	@Test
	public void testHonorific() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Evarts";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Evarts", c1.familyName);
		assertEquals("Mr", c1.honorificPrefix);
		new Repositories(new PersonNameMatcher()).COMIC_CHARACTERS.print();
	}

	@Test
	public void testHonorific3() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Mike Evarts";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertEquals("Mike", c1.givenName);
		assertEquals("Evarts", c1.familyName);
		assertEquals("Mr", c1.honorificPrefix);
		new Repositories(new PersonNameMatcher()).COMIC_CHARACTERS.print();
	}

	@Test
	public void testHonorificNeutralButMale() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain Jim";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertTrue(namesImporter.isMaleName("Jim"));

		assertTrue(namesImporter.maleCache.contains("Jim"));
		assertEquals("Jim", c1.givenName);
		assertEquals("Captain", c1.honorificPrefix);
	}
	@Test
	public void testNeutralPrefixNoGender() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain X";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertNull(c1.givenName);
		assertEquals("Captain", c1.honorificPrefix);
	}
	
	@Test
	public void testNeutralPrefixNoGenderWithLastName() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain Smith";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertNull(c1.givenName);
		assertEquals("Smith", c1.familyName);
		assertEquals("Captain", c1.honorificPrefix);
	}
	
	
	@Test
	public void testHonorificNeutralButFemale() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain Debra";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertTrue(namesImporter.isMaleName("Jim"));

		assertTrue(namesImporter.maleCache.contains("Jim"));
		assertEquals("Debra", c1.givenName);
		assertEquals("Captain", c1.honorificPrefix);
	}

	@Test
	public void testBadNameWithPrefix() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain 423";
		Repositories repositories = new Repositories(new PersonNameMatcher());
		repositories.COMIC_CHARACTERS.add(c1);

		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter,
				repositories);
		t.transform();

		assertTrue(namesImporter.isMaleName("Jim"));

		assertNull(c1.givenName);
		assertNull(c1.familyName);
		assertEquals("Captain", c1.honorificPrefix);

	}
}
