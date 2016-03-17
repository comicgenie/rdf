package org.comicwiki;

import java.io.File;

import org.comicwiki.guice.ComicWikiModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


final class MainETLRunner {
	
	private ETL etl;
	
	public MainETLRunner(ComicWikiModule module) {
		Injector injector = Guice.createInjector(module);
		etl = injector.getInstance(ETL.class);
		etl.setInjector(new WikiTableInjector(injector));
	}
	
	public void fromRDB(String jdbcUrl) throws Exception {
		etl.fromRDB(jdbcUrl);
	}
	
	public void start(File resourceIds, File outputDir) throws Exception {
		etl.process(resourceIds, outputDir);
	}
}
