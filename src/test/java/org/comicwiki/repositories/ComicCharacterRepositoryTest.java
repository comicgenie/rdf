package org.comicwiki.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.comicwiki.DataFormat;
import org.comicwiki.IRI;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.CreativeWorkExtension;
import org.comicwiki.transforms.ComicCharacterTransform;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jsonldjava.utils.JsonUtils;

public class ComicCharacterRepositoryTest {

	private static PersonNameMatcher namesImporter;

	@After
	 public void clean() {
		 Repositories.COMIC_CHARACTERS.clear();
	 }
	 
	@Test
	public void testMergeStringField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Superman";
		ComicCharacter c2 = new ComicCharacter();

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals("Superman", c2.name);
	}

	@Test
	public void testMergeNoOverrideStringField() throws Exception {
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Superman";
		ComicCharacter c2 = new ComicCharacter();
		c1.name = "Superman 2";

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals("Superman 2", c2.name);
	}

	@Test
	public void testMergeCollectionField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.alternateNames.add("A1");
		ComicCharacter c2 = new ComicCharacter();
		c2.alternateNames.add("A2");

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(2, c2.alternateNames.size());
		assertTrue(c2.alternateNames.contains("A1"));
		assertTrue(c2.alternateNames.contains("A2"));

		Repositories.COMIC_CHARACTERS.save(System.out, DataFormat.N_TRIPLES);
	}

	@Test
	public void testMergeCreativeField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.publisher = IRI.create("pub1");
		ComicCharacter c2 = new ComicCharacter();

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(IRI.create("pub1"), c2.creativeWork.publisher);
	}

	@Test
	public void testMergeCreativeFieldWithCollection() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.artists.add(IRI.create("ART1"));

		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.artists.add(IRI.create("ART2"));

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(2, c2.creativeWork.artists.size());
		assertTrue(c2.creativeWork.artists.contains(IRI.create("ART2")));
	}

	@Test
	public void testNoMergeCreativeField() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.publisher = IRI.create("pub1");
		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.publisher = IRI.create("pub2");

		Repositories.COMIC_CHARACTERS.merge(c1, c2);
		assertEquals(IRI.create("pub2"), c2.creativeWork.publisher);
		System.out.println(JsonUtils.toPrettyString(c2));
	}

	@Test
	public void testExport() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "X1";
		c1.alternateNames.add("A1");

		ComicCharacter c2 = new ComicCharacter();
		c2.name = "X2";
		c2.alternateNames.add("A2");

		Repositories.COMIC_CHARACTERS.add(c1);
		Repositories.COMIC_CHARACTERS.add(c2);
		Repositories.COMIC_CHARACTERS.save(System.out, DataFormat.JSON);
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
	public void testHonorific() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Evarts";
		Repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter);
		t.transform();
		
		assertEquals("Evarts", c1.familyName);
		assertEquals("Mr", c1.honorificPrefix);
		Repositories.COMIC_CHARACTERS.print();
	}
	
	@Test
	public void testHonorific3() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Mr. Mike Evarts";
		Repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter);
		t.transform();
		
		assertEquals("Mike", c1.givenName);
		assertEquals("Evarts", c1.familyName);
		assertEquals("Mr", c1.honorificPrefix);
		Repositories.COMIC_CHARACTERS.print();
	}
	
	@Test
	public void testHonorific2() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain Jim";
		Repositories.COMIC_CHARACTERS.add(c1);
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter);
		t.transform();
		Repositories.COMIC_CHARACTERS.print();
		
		assertTrue(namesImporter.isMaleName("Jim"));
		
		assertTrue(namesImporter.maleCache.contains("Jim"));
		assertEquals("Jim", c1.givenName);
		assertEquals("Captain", c1.honorificPrefix);
		
	}
	
	@Test
	public void testBadNameWithPrefix() throws Exception {

		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Captain 423";
		Repositories.COMIC_CHARACTERS.add(c1);
		
		ComicCharacterTransform t = new ComicCharacterTransform(namesImporter);
		t.transform();
		
		Repositories.COMIC_CHARACTERS.print();
		
		assertTrue(namesImporter.isMaleName("Jim"));
		
		assertNull(c1.givenName);
		assertNull(c1.familyName);
		assertEquals("Captain", c1.honorificPrefix);
		
	}
}
