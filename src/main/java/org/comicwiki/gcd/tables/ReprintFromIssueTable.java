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
import org.comicwiki.model.schema.bib.ComicStory;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * Issue has one of it's stories reprinted
 */
@Join(value = IssueTable.class, leftKey = "fkOriginIssueId", leftField = "originalIssue")
@Join(value = StoryTable.class, leftKey = "fkTargetStoryId", leftField = "reprintStory")
public class ReprintFromIssueTable extends
		BaseTable<ReprintFromIssueTable.ReprintFromIssueRow> {
	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("origin_issue_id"),
				new Column("target_id"), new Column("notes") };
		public static final int ID = 0;

		public static final int NOTES = 3;

		/**
		 * gcd_issue.id
		 */
		public static final int ORIGIN_ISSUE_ID = 1;

		/**
		 * gcd_story.id
		 */
		public static final int TARGET_ID = 2;

	}

	public static class ReprintFromIssueRow extends TableRow<ReprintNote> {
		public String notes;

		public int fkOriginIssueId;

		public int fkTargetStoryId;
		
		public ReprintNote instance = create(thingFactory);
		
		public ComicIssue originalIssue;
		
		public ComicStory reprintStory;
	}

	private static final String sInputTable = "gcd_reprint_from_issue";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public ReprintFromIssueTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		ReprintFromIssueTable.thingFactory = thingFactory;		
	}

	@Override
	public ReprintFromIssueRow process(Row row) throws IOException {
		ReprintFromIssueRow issueRow = new ReprintFromIssueRow();
		issueRow.notes = row.getString(Columns.NOTES);
		if (!row.isNullAt(Columns.ORIGIN_ISSUE_ID)) {
			issueRow.fkOriginIssueId = row.getInt(Columns.ORIGIN_ISSUE_ID);
		}
		if (!row.isNullAt(Columns.TARGET_ID)) {
			issueRow.fkTargetStoryId = row.getInt(Columns.TARGET_ID);
		}	
		if (!row.isNullAt(Columns.ID)) {
			issueRow.id = row.getInt(Columns.ID);
			add(issueRow);
		}
		return issueRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(ReprintFromIssueRow row) {
		super.transform(row);
		ReprintNote reprintNote = row.instance;
		
		if(row.originalIssue != null) {
			reprintNote.firstPrint = row.originalIssue.instanceId;
			row.originalIssue.reprintNote.add(reprintNote.instanceId);
		}
		
		if(row.reprintStory!= null) {
			reprintNote.reprint = row.reprintStory.instanceId;
			row.reprintStory.reprintNote.add(reprintNote.instanceId);
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {
			reprintNote.note.add(row.notes);
		}
	}
}
