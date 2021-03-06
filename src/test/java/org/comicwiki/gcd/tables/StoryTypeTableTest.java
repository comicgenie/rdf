package org.comicwiki.gcd.tables;

import static org.junit.Assert.assertEquals;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.junit.Test;

public class StoryTypeTableTest {

	@Test
	public void allNull() throws Exception {
		Row row = RowFactory.create(null, null);
		StoryTypeTable table = new StoryTypeTable(null);
		table.process(row);
		assertEquals(0, table.rowCache.size());
	}
	
	@Test
	public void one() throws Exception {
		Row row = RowFactory.create(1, "credits");
		StoryTypeTable table = new StoryTypeTable(null);
		table.process(row);
		assertEquals(1, table.rowCache.size());
		assertEquals("credits", table.rowCache.get(1).name);
	}
}
