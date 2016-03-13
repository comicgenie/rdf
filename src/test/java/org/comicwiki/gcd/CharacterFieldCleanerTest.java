package org.comicwiki.gcd;

import static org.junit.Assert.assertEquals;

import org.comicwiki.gcd.fields.CharacterFieldCleaner;
import org.junit.Test;

public class CharacterFieldCleanerTest {
//Batman [Bruce Wayne; also as Blair Graeme]
	
	@Test
	public void alsoAsInner() throws Exception {
		String text = "Batman [Bruce Wayne; also as Blair Graeme]";
		String result = CharacterFieldCleaner.alsoAs(text);
		assertEquals("Batman [Bruce Wayne;Blair Graeme]", result);		
	}
	
	@Test
	public void alsoAs() throws Exception {
		String text = "Batman [also as Bruce Wayne]";
		String result = CharacterFieldCleaner.alsoAs(text);
		assertEquals("Batman [Bruce Wayne]", result);		
	}
	
	@Test
	public void as() throws Exception {
		String text = "Batman [ as Bruce Wayne]";
		String result = CharacterFieldCleaner.alsoAs(text);
		System.out.println(result);
		assertEquals("Batman [Bruce Wayne]", result);		
	}
	
	@Test
	public void alsoAppearsAs() throws Exception {
		String text = "Batman [ also APpears as Bruce Wayne]";
		String result = CharacterFieldCleaner.alsoAs(text);
		System.out.println(result);
		assertEquals("Batman [Bruce Wayne]", result);		
	}
	

	@Test
	public void repairInnerNoChange() throws Exception {
		String text = "F [(N)]";
		assertEquals("F [(N)]", CharacterFieldCleaner.repair(text));
	}
	
	@Test
	public void repairOuterChange() throws Exception {
		String text = "F [(N))";
		System.out.println(CharacterFieldCleaner.repair(text));
		assertEquals("F [(N)]", CharacterFieldCleaner.repair(text));
	}
	
	@Test
	public void repairChange() throws Exception {
		String text = "F [N)";
		System.out.println(CharacterFieldCleaner.repair(text));
		assertEquals("F [N]", CharacterFieldCleaner.repair(text));
	}
	
	@Test
	public void repairFlatChange() throws Exception {
		String text = "F [N); A[1]";
		System.out.println(CharacterFieldCleaner.repair(text));
		assertEquals("F [N]; A[1]", CharacterFieldCleaner.repair(text));
	}
	
	@Test
	public void repairInnerChange() throws Exception {
		String text = "F [(N]]";
		assertEquals("F [(N)]", CharacterFieldCleaner.repair(text));
	}
	
	
}
