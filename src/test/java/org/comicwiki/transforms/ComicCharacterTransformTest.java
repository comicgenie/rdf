package org.comicwiki.transforms;

import static org.junit.Assert.assertEquals;

import org.comicwiki.PersonNameMatcher;
import org.comicwiki.Repositories;
import org.comicwiki.model.ComicCharacter;
import org.junit.Test;

public class ComicCharacterTransformTest {

	@Test
	public void transformOnlyPrefix() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacter cc = new ComicCharacter();
		cc.name = "Mr.";
		repositories.COMIC_CHARACTERS.add(cc);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		transform.transform();
	}

	@Test
	public void transformEmptyName() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacter cc = new ComicCharacter();
		cc.name = "";
		repositories.COMIC_CHARACTERS.add(cc);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		transform.transform();
	}

	@Test
	public void transformNullName() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		repositories.COMIC_CHARACTERS.add(new ComicCharacter());
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		transform.transform();
	}

	@Test
	public void testMalePrefix() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		assertEquals("X", transform.removePrefixAndSuffix("Mr. X"));
	}

	@Test
	public void testMaleSuffix() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		assertEquals("X", transform.removePrefixAndSuffix("Mr. X Esq."));
	}

	@Test
	public void testFemalePrefix() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		assertEquals("Matilda",
				transform.removePrefixAndSuffix("Queen Matilda"));
	}

	@Test
	public void testNeutralPrefix() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		assertEquals("Useless",
				transform.removePrefixAndSuffix("Senator Useless"));
	}

	@Test(expected = NullPointerException.class)
	public void testPrefixNull() throws Exception {
		PersonNameMatcher personMatcher = new PersonNameMatcher();
		Repositories repositories = new Repositories(personMatcher);
		ComicCharacterTransform transform = new ComicCharacterTransform(
				personMatcher, repositories);
		transform.removePrefixAndSuffix(null);
	}
}
