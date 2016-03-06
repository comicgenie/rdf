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
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.ReprintNote;
import org.comicwiki.model.schema.bib.ComicIssue;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * Will join into issue table
 * 
 * Issue to Issue
 * 
 */
@Join(value = IssueTable.class, leftKey = "fkOriginIssueId", leftField = "original")
@Join(value = IssueTable.class, leftKey = "fkTargetIssueId", leftField = "reprint")
public class IssueReprintTable extends
		BaseTable<IssueReprintTable.IssueReprintRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("origin_issue_id"),
				new Column("target_issue_id"), new Column("notes") };
		public static final int ID = 0;

		public static final int NOTES = 3;

		/**
		 * Original issue
		 * gcd_issue.id
		 */
		public static final int ORIGIN_ISSUE_ID = 1;

		/**
		 * New Issue that contains reprints
		 * gcd_issue.id
		 */
		public static final int TARGET_ISSUE_ID = 2;

	}

	public static class IssueReprintRow extends TableRow<ReprintNote> {
		
		public int fkOriginIssueId;
		
		public int fkTargetIssueId;

		public ReprintNote instance = create(thingFactory);

		public String notes;
		
		public ComicIssue original;
		
		public ComicIssue reprint;
	}

	private static final String sInputTable = "gcd_issue_reprint";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public IssueReprintTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
	}

	@Override
	public IssueReprintRow process(Row row) throws IOException {
		IssueReprintRow issueReprintRow = new IssueReprintRow();
		issueReprintRow.notes = row.getString(Columns.NOTES);
		if (!row.isNullAt(Columns.ORIGIN_ISSUE_ID)) {
			issueReprintRow.fkOriginIssueId = row.getInt(Columns.ORIGIN_ISSUE_ID);
		}
		if (!row.isNullAt(Columns.TARGET_ISSUE_ID)) {
			issueReprintRow.fkTargetIssueId= row.getInt(Columns.TARGET_ISSUE_ID);		
		}
		if (!row.isNullAt(Columns.ID)) {
			issueReprintRow.id = row.getInt(Columns.ID);
			add(issueReprintRow);
		}
		return issueReprintRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(IssueReprintRow row) {
		super.transform(row);
		ReprintNote reprintNote = row.instance;
		
		if(row.original != null) {
			reprintNote.print = row.original.instanceId;
		}
		
		if(row.reprint != null) {
			reprintNote.reprint = row.reprint.instanceId;
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {
			reprintNote.note.add(row.notes);
		}
	}
	
	
}
