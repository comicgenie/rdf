grammar Creator;   
@header {
 //   package org.comicwiki.gcd.parser;
}  

creators
	: creator (DELIM creator)*?
	;
	
creator 
	: WORD  (ALIAS? NOTES?) | (NOTES? ALIAS? ) 
	;
	
ALIAS 
	: ('['.*?']')
	{ setText(getText().substring(1, getText().length() - 1 ).trim());}	
	;  

NOTES
	:  ('('.*?')')
	{ setText(getText().substring(1, getText().length() - 1 ).trim());}	
	;

WORD 
	: ~('['|']'|'('|')'|';')+
		{ setText(getText().trim());}	
	;

DELIM
	: ';'
	;
	
WS: [ \t\r\n]+ -> skip ;