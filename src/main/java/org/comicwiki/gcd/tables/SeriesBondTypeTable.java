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

import com.google.inject.Inject;

public class SeriesBondTypeTable extends
		BaseTable<SeriesBondTypeTable.SeriesBondTypeRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"),
				new Column("description"), new Column("notes") };
		public static final int DESCRIPTION = 2;

		public static final int ID = 0;

		public static final int NAME = 1;

		public static final int NOTES = 3;
	}

	public static class SeriesBondTypeRow extends TableRow {
		public String description;

		public String name;

		public String notes;
	}

	private static final String sInputTable = "gcd_series_bond_type";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public SeriesBondTypeTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public SeriesBondTypeRow process(Row row) throws IOException {
		SeriesBondTypeRow bondTypeRow = new SeriesBondTypeRow();
		bondTypeRow.id = row.getInt(Columns.ID);
		bondTypeRow.description = row.getString(Columns.DESCRIPTION);
		bondTypeRow.name = row.getString(Columns.NAME);
		bondTypeRow.notes = row.getString(Columns.NOTES);
		return bondTypeRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
