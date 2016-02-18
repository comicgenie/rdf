package org.comicwiki.gcd.tables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.IRICache;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.CharacterCreator;
import org.comicwiki.gcd.CharacterFieldParser;
import org.comicwiki.gcd.CharacterWalker;
import org.comicwiki.gcd.tables.StoryTable.Fields;
import org.comicwiki.gcd.tables.StoryTable.StoryRow;

import com.google.inject.Inject;

public class CharactersView extends BaseTable<StoryTable.StoryRow> {

	private static final class Columns {
		public static final int CHARACTERS = 1;

		public static final Column[] COMIC_CHARACTERS = new Column[] {
				new Column("id"), new Column("characters") };
	}

	private static final String sInputTable = "gcd_story";

	private static final String sParquetName = "characters.parquet";

	private boolean failOnParse;

	private FileOutputStream fos;

	private ThingFactory thingFactory;

	private CharacterCreator characterCreator;

	private File resourceDir;

	private final IRICache iriCache;

	@Inject
	public CharactersView(SQLContext sqlContext, boolean failOnParse,
			ThingFactory thingFactory, IRICache iriCache,
			CharacterCreator characterCreator) {
		super(sqlContext, sParquetName);
		this.iriCache = iriCache;
		this.thingFactory = thingFactory;
		this.characterCreator = characterCreator;
		this.resourceDir = new File(".");
	}

	@Inject
	public CharactersView(SQLContext sqlContext, IRICache iriCache,
			boolean failOnParse) {
		super(sqlContext, sParquetName);
		this.iriCache = iriCache;
		this.failOnParse = failOnParse;
		try {
			fos = new FileOutputStream(new File("characters.error.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public StoryRow process(Row row) throws IOException {
		StoryRow storyRow = new StoryTable.StoryRow();
		Fields.Character characterField = parseField(Columns.CHARACTERS, row,
				new CharacterFieldParser(new CharacterWalker(thingFactory,
						iriCache, characterCreator, resourceDir)));

		if (characterField != null) {
			storyRow.characters = characterField.comicCharacters;
			storyRow.organizations = characterField.comicOrganizations;
		}
		return storyRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.COMIC_CHARACTERS,
				jdbcUrl, sParquetName);
	}
}
