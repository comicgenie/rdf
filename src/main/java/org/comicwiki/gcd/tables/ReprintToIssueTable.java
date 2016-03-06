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
 * Reprint of story in another issue
 */
@Join(value = StoryTable.class, leftKey = "fkOriginStoryId", leftField = "originalStory")
@Join(value = IssueTable.class, leftKey = "fkTargetIssueId", leftField = "reprintIssue")
public class ReprintToIssueTable extends
		BaseTable<ReprintToIssueTable.ReprintToIssueRow> {

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("origin_id"),
				new Column("target_issue_id"), new Column("notes") };
		public static final int ID = 0;

		public static final int NOTES = 3;

		/**
		 * gcd_story.id
		 */
		public static final int ORIGIN_ID = 1;

		/**
		 * gcd_issue.id
		 */
		public static final int TARGET_ISSUE_ID = 2;
	}

	public static class ReprintToIssueRow extends TableRow<ReprintNote> {
		public String notes;

		public int fkOriginStoryId;

		public int fkTargetIssueId;
		
		public ReprintNote instance = create(thingFactory);
		
		public ComicStory originalStory;
		
		public ComicIssue reprintIssue;
	}

	private static final String sInputTable = "gcd_reprint_to_issue";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public ReprintToIssueTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
	}

	@Override
	public ReprintToIssueRow process(Row row) throws IOException {
		ReprintToIssueRow issueRow = new ReprintToIssueRow();
		issueRow.notes = row.getString(Columns.NOTES);
		if (!row.isNullAt(Columns.ORIGIN_ID)) {
			issueRow.fkOriginStoryId = row.getInt(Columns.ORIGIN_ID);
		}
		if (!row.isNullAt(Columns.TARGET_ISSUE_ID)) {
			issueRow.fkTargetIssueId = row.getInt(Columns.TARGET_ISSUE_ID);
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
	protected void transform(ReprintToIssueRow row) {
		super.transform(row);
		ReprintNote reprintNote = row.instance;
		
		if(row.originalStory != null) {
			reprintNote.firstPrint = row.originalStory.instanceId;
			row.originalStory.reprintNote.add(reprintNote.instanceId);
		}
		
		if(row.reprintIssue != null) {
			reprintNote.reprint = row.reprintIssue.instanceId;
			row.reprintIssue.reprintNote.add(reprintNote.instanceId);
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {
			reprintNote.note.add(row.notes);
		}
	}	
}
