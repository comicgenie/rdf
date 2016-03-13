package org.comicwiki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.comicwiki.gcd.tables.BrandEmblemGroupTable;
import org.junit.Test;

import com.google.inject.Injector;

public class ETLTest {

	@Test
	public void process() throws Exception {
		Injector injector = mock(Injector.class);
		BrandEmblemGroupTable mockTable = mock(BrandEmblemGroupTable.class);
		when(injector.getInstance(BrandEmblemGroupTable.class)).thenReturn(
				mockTable);

		ResourceIDCache resourceIDCache = new ResourceIDCache();
		ThingCache thingCache = new ThingCache(new Repositories(
				new PersonNameMatcher()), new IRICache(), resourceIDCache);
		ETL etl = new ETL(thingCache,
				new Repositories(new PersonNameMatcher()),
				new ResourceIDCache(), null);
		etl.setInjector(injector);
		etl.process(new File("."), new File("."));
		verify(mockTable, times(1)).extract();

	}

	@Test
	public void fromRDB() throws Exception {
		Injector injector = mock(Injector.class);
		BrandEmblemGroupTable mockTable = mock(BrandEmblemGroupTable.class);
		when(injector.getInstance(BrandEmblemGroupTable.class)).thenReturn(
				mockTable);

		ETL etl = new ETL(null, null, null, null);
		etl.setInjector(injector);
		etl.fromRDB("jdbc:mysql//localhost");
		verify(mockTable, times(1))
				.saveToParquetFormat("jdbc:mysql//localhost");

	}

	@Test
	public void loadTables() throws Exception {
		ETL etl = new ETL(null, null, null, null);
		Injector injector = mock(Injector.class);
		BrandEmblemGroupTable mockTable = mock(BrandEmblemGroupTable.class);
		when(injector.getInstance(BrandEmblemGroupTable.class)).thenReturn(
				mockTable);
		BaseTable[] tables = ETL.getTables(injector);
		assertNotNull(tables);
		assertEquals(ETL.tableClasses.length, tables.length);
		assertNotNull(tables[0]);

	}

	@Test(expected = IllegalArgumentException.class)
	public void noOutputDir() throws Exception {
		ETL etl = new ETL(null, null, null, null);
		etl.process(new File("."), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noResourceIds() throws Exception {
		ETL etl = new ETL(null, null, null, null);
		etl.process(null, new File("."));
	}
}
