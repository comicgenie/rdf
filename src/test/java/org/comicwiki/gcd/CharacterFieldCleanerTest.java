package org.comicwiki.gcd;

import static org.junit.Assert.*;

import org.comicwiki.gcd.CharacterFieldCleaner;
import org.junit.Test;

public class CharacterFieldCleanerTest {

	@Test
	public void alsoAs() throws Exception {
		String text = "Batman (also as Bruce Wayne)";
		String result = CharacterFieldCleaner.alsoAs(text);
		assertEquals("Batman (Bruce Wayne)", result);		
	}
	
	@Test
	public void as() throws Exception {
		String text = "Batman ( as Bruce Wayne)";
		String result = CharacterFieldCleaner.alsoAs(text);
		System.out.println(result);
		assertEquals("Batman (Bruce Wayne)", result);		
	}
	
	@Test
	public void alsoAppearsAs() throws Exception {
		String text = "Batman ( also APpears as Bruce Wayne)";
		String result = CharacterFieldCleaner.alsoAs(text);
		System.out.println(result);
		assertEquals("Batman (Bruce Wayne)", result);		
	}
}
