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

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class ReprintToIssueTable extends
		BaseTable<ReprintToIssueTable.ReprintToIssueRow> {
	public static class ReprintToIssueRow extends TableRow {
		public String notes;

		public int originIssueId;

		public int targetIssueId;
	}

	private static final String sInputTable = "gcd_reprint_to_issue";

	private static final String sParquetName = sInputTable + ".parquet";

	public ReprintToIssueTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public ReprintToIssueRow process(Row row) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		// TODO Auto-generated method stub

	}
}