package org.comicwiki;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResourceIDCacheTest {
	@Test
	public void setIndex() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", "@I5");
		cache.put("#abct", "@I10");
	
		assertEquals(10, cache.setIndex());
		assertEquals("@I11", cache.generateResourceId());
	}
	
	@Test
	public void get() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", "@I5");
		cache.put("#abct", "@I10");
	
		assertEquals("@I5", cache.get("#abcs"));
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void setIndexWithNullValue() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", null);
		cache.setIndex();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getNumberWithNoAtSign() throws Exception {
		ResourceIDCache.getNumber("I1234");
	}
	
	@Test
	public void getNumber() throws Exception {
		long number = ResourceIDCache.getNumber("@I1234");
		assertEquals(1234L, number);
	}
	
	
}
