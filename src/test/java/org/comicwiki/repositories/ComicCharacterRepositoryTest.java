package org.comicwiki.repositories;

import static org.junit.Assert.*;

import org.comicwiki.DataFormat;
import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.CreativeWorkExtension;
import org.junit.Test;

import com.github.jsonldjava.utils.JsonUtils;

public class ComicCharacterRepositoryTest {

	@Test
	public void testMergeStringField() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Superman";
		ComicCharacter c2 = new ComicCharacter();
		
		repo.merge(c1, c2);
		assertEquals("Superman", c2.name);
	}
	
	@Test
	public void testMergeNoOverrideStringField() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "Superman";
		ComicCharacter c2 = new ComicCharacter();
		c1.name = "Superman 2";
		
		repo.merge(c1, c2);
		assertEquals("Superman 2", c2.name);
	}
	
	@Test
	public void testMergeCollectionField() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.alternateNames.add("A1");
		ComicCharacter c2 = new ComicCharacter();
		c2.alternateNames.add("A2");
		
		repo.merge(c1, c2);
		assertEquals(2, c2.alternateNames.size());
		assertTrue(c2.alternateNames.contains("A1"));
		assertTrue(c2.alternateNames.contains("A2"));
		
		repo.exportData(System.out, DataFormat.N_TRIPLES);
	}
	
	@Test
	public void testMergeCreativeField() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.publisher = "pub1";
		ComicCharacter c2 = new ComicCharacter();
		
		repo.merge(c1, c2);
		assertEquals("pub1", c2.creativeWork.publisher);
	}
	
	@Test
	public void testMergeCreativeFieldWithCollection() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.artists.add("ART1");
		
		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.artists.add("ART2");
		
		repo.merge(c1, c2);
		assertEquals(2, c2.creativeWork.artists.size());
		assertTrue(c2.creativeWork.artists.contains("ART2"));
	}
	
	
	@Test
	public void testNoMergeCreativeField() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.creativeWork = new CreativeWorkExtension();
		c1.creativeWork.publisher = "pub1";
		ComicCharacter c2 = new ComicCharacter();
		c2.creativeWork.publisher = "pub2";
		
		repo.merge(c1, c2);
		assertEquals("pub2", c2.creativeWork.publisher);
		System.out.println(JsonUtils.toPrettyString(c2));
	}
	
	@Test
	public void testExport() throws Exception {
		ComicCharacterRepository repo = new ComicCharacterRepository();
		
		ComicCharacter c1 = new ComicCharacter();
		c1.name = "X1";
		c1.alternateNames.add("A1");
		
		ComicCharacter c2 = new ComicCharacter();
		c2.name = "X2";
		c2.alternateNames.add("A2");
		
		repo.add(c1);
		repo.add(c2);
		//repo.exportData(System.out, DataFormat.JSON);
		repo.exportData(System.out, DataFormat.N_TRIPLES);
	}
}
