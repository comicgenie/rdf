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

public class SeriesBondTable extends BaseTable<SeriesBondTable.SeriesBondRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("origin_id"),
				new Column("target_id"), new Column("origin_issue_id"),
				new Column("target_issue_id"), new Column("bond_type_id") };
		public static final int BOND_TYPE_ID = 5;

		public static final int ID = 0;

		public static final int ORIGIN_ID = 1;

		public static final int ORIGIN_ISSUE_ID = 3;

		public static final int TARGET_ID = 2;

		public static final int TARGET_ISSUE_ID = 4;
	}

	public static class SeriesBondRow extends TableRow {
		public int bondTypeId;

		public int originId;

		public int originIssueId;

		public int targetId;

		public int targetIssueId;
	}

	private static final String sInputTable = "gcd_series_bond";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public SeriesBondTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public SeriesBondRow process(Row row) throws IOException {
		SeriesBondRow bondRow = new SeriesBondRow();
		bondRow.id = row.getInt(Columns.ID);
		bondRow.bondTypeId = row.getInt(Columns.BOND_TYPE_ID);
		bondRow.originId = row.getInt(Columns.ORIGIN_ID);
		bondRow.originIssueId = row.getInt(Columns.ORIGIN_ISSUE_ID);
		bondRow.targetId = row.getInt(Columns.TARGET_ID);
		bondRow.targetIssueId = row.getInt(Columns.TARGET_ISSUE_ID);
		return bondRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
