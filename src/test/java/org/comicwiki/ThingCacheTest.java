package org.comicwiki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.comicwiki.model.schema.Person;
import org.comicwiki.model.schema.Thing;
import org.comicwiki.rdf.annotations.ObjectIRI;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;
import org.junit.Test;

public class ThingCacheTest {

	@Subject(value = "TestClass", compositeKey = { "name", "familyName" })
	private class TestClass extends Person {

		public TestClass() {
			this.resourceId = new IRI("@N123");
		}
	}

	@Subject(value = "TestClass", compositeKey = { "name", "site" })
	private class TestClass2 extends Person {

		public TestClass2() {
			this.resourceId = new IRI("@N123");
		}

		@Predicate("site")
		@ObjectIRI
		@SchemaComicWiki
		public IRI site;
	}

	@Test
	public void readCompositePropertyKey() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());
		TestClass tc = new TestClass();
		tc.familyName = "Smith";
		tc.name = "Mr. Smith";
		String cpk = thingCache.readCompositePropertyKey(tc);

		assertEquals(
				DigestUtils.md5Hex(TestClass.class.getCanonicalName()
						+ ":name:Mr. Smith_familyName:Smith_"), cpk);
	}

	@Test
	public void readCompositePropertyKeyWithIRIKey() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());

		TestClass tc = new TestClass();
		tc.familyName = "Smith";
		tc.name = "Mr. Smith";
		thingCache.add(tc);

		TestClass2 tc2 = new TestClass2();
		tc2.site = tc.instanceId;
		tc2.name = "foo";
		thingCache.add(tc2);

		String cpk = thingCache.readCompositePropertyKey(tc2);

		String tcId = DigestUtils.md5Hex(TestClass.class.getCanonicalName()
				+ ":name:Mr. Smith_familyName:Smith_");
		assertEquals(
				DigestUtils.md5Hex(TestClass2.class.getCanonicalName()
						+ ":name:foo_" + tcId + "_"), cpk);
	}

	@Test
	public void readCompositePropertyKeyWithNull() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());

		TestClass tc = new TestClass();
		tc.name = "Mr. Smith";
		String cpk = thingCache.readCompositePropertyKey(tc);

		assertEquals(
				DigestUtils.md5Hex(TestClass.class.getCanonicalName()
						+ ":name:Mr. Smith_"), cpk);
	}

	@Test
	public void add() throws Exception {
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), new ResourceIDCache());
		Thing thing = new Thing();
		thingCache.add(thing);

		assertNotNull(thing.instanceId);
		assertEquals(1, thingCache.instanceCache.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void assignResourceIdsNoKey() throws Exception {
		IRICache iriCache = new IRICache();
		ResourceIDCache resourceIDCache = new ResourceIDCache();
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), iriCache, resourceIDCache);

		Thing thing = new Thing();
		thingCache.add(thing);
		thingCache.assignResourceIDs();
	}

	@Test
	public void assignResourceIdsSameKey() throws Exception {
		IRICache iriCache = new IRICache();
		ResourceIDCache resourceIDCache = new ResourceIDCache();
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), iriCache, resourceIDCache);

		Thing thing = new Thing();
		thing.name = "foo";

		Thing thing2 = new Thing();
		thing2.name = "foo2";
		thingCache.add(thing);
		thingCache.add(thing2);
		thingCache.assignResourceIDs();

		resourceIDCache.exportResourceIDs(null);

	}

}
