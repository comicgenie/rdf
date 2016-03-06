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
import org.comicwiki.model.schema.bib.ComicStory;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * Reprints of stories
 */
@Join(value = StoryTable.class, leftKey = "fkOriginId", leftField = "original")
@Join(value = StoryTable.class, leftKey = "fkTargetId", leftField = "reprint")
public class ReprintTable extends BaseTable<ReprintTable.ReprintRow> {

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("origin_id"),
				new Column("target_id"), new Column("notes") };
		public static final int ID = 0;

		public static final int NOTES = 3;

		/**
		 * gcd_story.id
		 */
		public static final int ORIGIN_ID = 1;

		/**
		 * gcd_story.id
		 */
		public static final int TARGET_ID = 2;
	}

	public static class ReprintRow extends TableRow<ReprintNote> {
		
		public int fkOriginId;
		
		public int fkTargetId;
		
		public ReprintNote instance = create(thingFactory);

		public String notes;
		
		public ComicStory original;
		
		public ComicStory reprint;
	}

	private static final String sInputTable = "gcd_reprint";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public ReprintTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		ReprintTable.thingFactory = thingFactory;
	}

	@Override
	public ReprintRow process(Row row) throws IOException {
		ReprintRow reprintRow = new ReprintRow();
		reprintRow.notes = row.getString(Columns.NOTES);
		if (!row.isNullAt(Columns.ORIGIN_ID)) {
			reprintRow.fkOriginId = row.getInt(Columns.ORIGIN_ID);
		}
		if (!row.isNullAt(Columns.TARGET_ID)) {
			reprintRow.fkTargetId = row.getInt(Columns.TARGET_ID);
		}	
		
		if (!row.isNullAt(Columns.ID)) {
			reprintRow.id = row.getInt(Columns.ID);
			add(reprintRow);
		}
		return reprintRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(ReprintRow row) {
		super.transform(row);
		ReprintNote reprintNote = row.instance;
		
		if(row.original != null) {
			reprintNote.firstPrint = row.original.instanceId;
		}
		
		if(row.reprint != null) {
			reprintNote.reprint = row.reprint.instanceId;
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {
			reprintNote.note.add(row.notes);
		}
	}
	
	
}
