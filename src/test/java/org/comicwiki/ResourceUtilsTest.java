package org.comicwiki;

import static org.junit.Assert.*;

import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class ResourceUtilsTest {

	@Test
	public void readResourceId() throws Exception {
		Thing thing = new Thing();
		thing.resourceId = "@1";
		String uri = ResourceUtils.readResourceIDWithExpandedIri(thing);
		assertEquals("http://comicwiki.org/resources/1", uri);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void readResourceIdWithNoAtSign() throws Exception {
		Thing thing = new Thing();
		thing.resourceId = "1";
		ResourceUtils.readResourceIDWithExpandedIri(thing);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void readResourceIdWithNoResourceID() throws Exception {
		Thing thing = new Thing();
		ResourceUtils.readResourceIDWithExpandedIri(thing);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void readResourceIdWithNull() throws Exception {
		Thing thing = new Thing();
		ResourceUtils.readResourceIDWithExpandedIri(thing);
	}
	
	
}
