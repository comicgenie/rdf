grammar CharacterField;     
@header {
//    package org.comicwiki.gcd.parser;
}  

root: characters*;

characters
	: organizationOrCharacter (DELIM organizationOrCharacter)*
	;

organization
	: WORD ORGANIZATION_MARKER characters
	; 

organizationOrCharacter
	: (organization | noteTypes? WORD membersOrAliases? note?) 
	| (organization | noteTypes? WORD note? membersOrAliases?)
	;

membersOrAliases
   : LEFT_BRACKET organizationOrCharacter (DELIM organizationOrCharacter)* RIGHT_BRACKET
   | LEFT_BRACKET RIGHT_BRACKET
   ;

note
   : LEFT_PAREN WORD (DELIM WORD)* RIGHT_PAREN
   | LEFT_PAREN RIGHT_PAREN
   ;  

noteTypes
	: VILLIAN 
	| GUEST 
	| CAMEO 
	| CAMEO_FLASHBACK 
	| INTRODUCTION 
	| FEATURE 
	| GUEST_APPEARANCE
	;
    
DELIM 
	: (';' | ',')
	;
	
ORGANIZATION_MARKER
	: '<EM_DASH>'
	;

WORD 
	: ~('['|']'|'('|')'|';'|','|':'|'<'|'>')+ 
	{ setText(getText().trim());}
	;

GUEST
	: [ ]*(G U E S T | G U E S T S | G) ':' 
	{ setText("GUEST");}
	;

VILLIAN
	: [ ]*(V I L L A I N | V I L L A I N S | V) ':' 
	{ setText("VILLIAN");}
	;

CAMEO
	: [ ]*(C A M E O | C A M E O S | C) ':' 
	{ setText("CAMEO");}
	;
 
CAMEO_FLASHBACK
	: [ ]*(C A M E O ' ' F L A S H B A C K | C A M E O ' ' F L A S H B A C K S) ':' 
	{ setText("CAMEO FLASHBACK");}
	;

INTRODUCTION
	: [ ]*(I N T R O D U C T I O N | I N T R O D U C T I O N S | I) ':' 
	{ setText("INTRODUCTION");}
	;

FEATURE
	: [ ]*(F E A T U R E | F E A T U R E S | F) ':' 
	{ setText("FEATURE");}
	;

GUEST_APPEARANCE
	: [ ]*(G U E S T ' ' A P P E A R A N C E| G U E S T ' ' A P P E A R A N C E S) ':' 
	{ setText("GUEST");}
	; 

WS 
	: [ \t\n\r] + -> skip
	;
COLON
	: ':' 
	;
SEMICOLON
	: ';' 
	;
COMMA 
	: ',' 
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
	
fragment A
	:('a'|'A');
fragment B
	:('b'|'B');
fragment C
	:('c'|'C');
fragment D
	:('d'|'D');
fragment E
	:('e'|'E');
fragment F
	:('f'|'F');
fragment G
	:('g'|'G');
fragment H
	:('h'|'H');
fragment I
	:('i'|'I');
fragment J
	:('j'|'J');
fragment K
	:('k'|'K');
fragment L
	:('l'|'L');
fragment M
	:('m'|'M');
fragment N
	:('n'|'N');
fragment O
	:('o'|'O');
fragment P
	:('p'|'P');
fragment Q
	:('q'|'Q');
fragment R
	:('r'|'R');
fragment S
	:('s'|'S');
fragment T
	:('t'|'T');
fragment U
	:('u'|'U');
fragment V
	:('v'|'V');
fragment W
	:('w'|'W');
fragment X
	:('x'|'X');
fragment Y
	:('y'|'Y');
fragment Z
	:('z'|'Z');

