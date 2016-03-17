import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.comicwiki.SparkUtils;
import org.comicwiki.guice.ComicWikiModule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {

	public int myint = 5;
	
	//@Test
	public void extract() throws Exception {
		/*
		Injector injector = Guice.createInjector(new ComicWikiModule(null));
		ETL etl = injector.getInstance(ETL.class);
	//	etl.setInjector(injector);
		String jdbcUrl = SparkUtils.createJDBCUrl("root", "MyNewPass", "my_wiki");
		etl.fromRDB(jdbcUrl);
		*/
	}
	
	@Test
	public void reflect() throws Exception {
		Field rf = getClass().getField("myint");
		assertEquals(5, rf.getInt(new Main()));
	}
}
