grammar IssueNumber;   
@header {
 //   package org.comicwiki.gcd.parser;
}  

issue 
	: (NO_NUMBER | ISSUE_NUMBER  | ISSUE_NUMBER  '/' YEAR | ISSUE_NUMBER  ' / ' ISSUE_NUMBER)? ASSIGNED? COVER? 
	;

NO_NUMBER 
	: '[nn]'
	;
ASSIGNED
	: LEFT_BRACKET .*? RIGHT_BRACKET
	{ setText(getText().substring(1, getText().length() -1).replaceAll("#", "").trim());}
	;
	
COVER
	: LEFT_PAREN .*? RIGHT_PAREN
	{ setText(getText().substring(1, getText().length() - 1 ).replaceAll("#", "").trim());}
	;
	
YEAR
	: DIGIT DIGIT DIGIT DIGIT
	;

ISSUE_NUMBER
	:   DIGIT+
		{ setText(getText().replaceAll("#", "").trim());}
	;
	
DIGIT   
	:   ('0'..'9')
	;
		
SEMICOLON 
	: ';'
	;

LEFT_PAREN
	: '(' 
	;
RIGHT_PAREN
	: ')' 
	;
LEFT_BRACKET
	: '[' 
	;
RIGHT_BRACKET
	: ']' 
	;
	
WS: [ \t\r\n]+ -> skip ;