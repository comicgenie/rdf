package org.comicwiki.guice;

import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcd.SparkUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ComicWikiModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	SQLContext provideSqlContext() {
		return SparkUtils.createLocalContext();
	}
}
