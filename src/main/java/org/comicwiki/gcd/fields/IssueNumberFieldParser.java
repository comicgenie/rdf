package org.comicwiki.gcd.fields;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.spark.sql.Row;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.FieldParser;
import org.comicwiki.gcd.parser.IssueNumberLexer;
import org.comicwiki.gcd.parser.IssueNumberParser;
import org.comicwiki.gcd.parser.IssueNumberParser.IssueContext;
import org.comicwiki.model.ComicIssueNumber;

import com.google.common.base.Strings;

public class IssueNumberFieldParser implements FieldParser<ComicIssueNumber> {

	private final ThingFactory thingFactory;

	private static IssueContext getContextOf(String textField,
			boolean failOnError) {
		IssueNumberLexer lexer = new IssueNumberLexer(new ANTLRInputStream(
				textField));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IssueNumberParser parser = new IssueNumberParser(tokens);
		if (failOnError) {
			// parser.setErrorHandler(new BailErrorStrategy());
		}
		return parser.issue();
	}

	public IssueNumberFieldParser(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}

	@Override
	public ComicIssueNumber parse(int field, Row row) {
		if (row.isNullAt(field)) {
			return null;
		}
		return parser(row.getString(field));
	}

	private static String getValue(TerminalNode node) {
		return node == null ? null : node.getText();
	}

	protected ComicIssueNumber parser(String textField) {
		if (Strings.isNullOrEmpty(textField)) {
			return null;
		}
		ComicIssueNumber issueNumber = thingFactory
				.create(ComicIssueNumber.class);
		issueNumber.label = textField;

		try {
			IssueContext issueContext = getContextOf(textField, true);
			if(issueContext.NO_NUMBER() != null) {
				return issueNumber;
			}
			String assigned = getValue(issueContext.ASSIGNED());
			String cover = getValue(issueContext.COVER());
			String year = getValue(issueContext.YEAR());

			if (!Strings.isNullOrEmpty(assigned)) {
				issueNumber.assigned = assigned;
			}

			if (!Strings.isNullOrEmpty(cover)) {
				issueNumber.cover = cover;
			}

			if (!Strings.isNullOrEmpty(year)) {
				issueNumber.year = year;
			}

			if (issueContext.ISSUE_NUMBER() != null) {
				for (int i = 0; i < issueContext.ISSUE_NUMBER().size(); i++) {
					issueNumber.issueNumbers.add(issueContext.ISSUE_NUMBER()
							.get(i).getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			issueNumber.isNonStandardGCDFormat = true;
		}
		return issueNumber;

	}

}
