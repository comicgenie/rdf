/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki.gcd.tables;

import java.io.IOException;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.schema.Language;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LanguageTable extends BaseTable<LanguageTable.LanguageRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("code"), new Column("name") };
		public static final int CODE = 1;
		public static final int ID = 0;

		public static final int NAME = 2;
	}

	public class LanguageRow extends TableRow<Language> {

		public Language instance = create(thingFactory);

	}

	private static final String sInputTable = "gcd_language";

	private static final String sParquetName = sInputTable + ".parquet";

	private ThingFactory thingFactory;

	@Inject
	public LanguageTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
	}

	@Override
	public LanguageRow process(Row row) throws IOException {
		LanguageRow languageRow = new LanguageRow();
		languageRow.instance.name = row.getString(Columns.NAME);
		languageRow.instance.languageCode = row.getString(Columns.CODE);

		if (!row.isNullAt(Columns.ID)) {
			languageRow.id = row.getInt(Columns.ID);
			add(languageRow);
		}
		return languageRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
