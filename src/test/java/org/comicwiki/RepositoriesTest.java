package org.comicwiki;

import org.comicwiki.model.ComicCharacter;
import org.comicwiki.model.schema.Thing;
import org.junit.Test;

public class RepositoriesTest {

	@Test
	public void getRepoAndAdd() throws Exception {
		Repository<Thing> repo = new Repositories().getRepository(ComicCharacter.class);
		ComicCharacter cc = new ComicCharacter();
		repo.add(cc);
	}
}
