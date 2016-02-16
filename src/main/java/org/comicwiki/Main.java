package org.comicwiki;

import org.comicwiki.guice.ComicWikiModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public void start() {
		Injector injector = Guice.createInjector(new ComicWikiModule());
		ETL etl = injector.getInstance(ETL.class);
		etl.setInjector(injector);
		//etl.process(resourceIds, outputDir);
	}
}
