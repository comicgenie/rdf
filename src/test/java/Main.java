import org.comicwiki.ETL;
import org.comicwiki.gcd.SparkUtils;
import org.comicwiki.guice.ComicWikiModule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {

	//@Test
	public void extract() throws Exception {
		Injector injector = Guice.createInjector(new ComicWikiModule());
		ETL etl = injector.getInstance(ETL.class);
		etl.setInjector(injector);
		String jdbcUrl = SparkUtils.createJDBCUrl("root", "MyNewPass", "my_wiki");
		etl.fromRDB(jdbcUrl);
	}
}
