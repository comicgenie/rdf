package org.comicwiki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResourceIDCacheTest {
	@Test
	public void setIndex() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", new IRI("@N5"));
		cache.put("#abct", new IRI("@N10"));
	
		assertEquals(10, cache.setIndex().n);
		assertEquals("@N11", cache.generateResourceId());
	}
	
	@Test
	public void get() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", new IRI("@N5"));
		cache.put("#abct", new IRI("@N10"));
	
		assertEquals(new IRI("@N5"), cache.get("#abcs"));
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void setIndexWithNullValue() throws Exception {
		ResourceIDCache cache = new ResourceIDCache();
		cache.put("#abcs", null);
		cache.setIndex();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getNumberWithNoAtSign() throws Exception {
		ResourceIDCache.getNumber("N1234");
	}
	
	@Test
	public void getNumber() throws Exception {
		long number = ResourceIDCache.getNumber("@N1234").n;
		assertEquals(1234L, number);
	}
	
	
}
