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

import com.google.inject.Inject;

public class LanguageTable extends BaseTable<LanguageTable.LanguageRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("code"), new Column("name") };
		public static final int CODE = 1;
		public static final int ID = 0;

		public static final int NAME = 2;
	}

	public static class LanguageRow extends TableRow {

		public String code;

		public String name;
	}

	private static final String sInputTable = "gcd_language";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public LanguageTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public LanguageRow process(Row row) throws IOException {
		LanguageRow languageRow = new LanguageRow();
		languageRow.id = row.getInt(Columns.ID);
		languageRow.name = row.getString(Columns.NAME);
		languageRow.code = row.getString(Columns.CODE);

		add(languageRow);
		return languageRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
