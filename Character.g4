grammar Character;       
characters  : character ((SEMICOLON|' and ')? character)*(SEMICOLON|' and ')?; 
teams : team+; 
team : 
	  characters  			#noTeam
    | TEAM characters			#teamAlt
	| WORD ('[' characters ']')  #teamStandard
	;
aliases : ('['SEMICOLON?WORD (SEMICOLON WORD)*SEMICOLON?']')?;  
notes :  ('('COMMA?WORD(COMMA WORD)*COMMA?')')?;
character : NOTE_TYPE? WORD ((aliases ' '* notes) | (notes ' '* aliases));    

SEMICOLON: ';';
COMMA: ',';
WORD : ~('['|']'|'('|')'|';'|',')+;
TEAM : WORD EM_DASH;  
NOTE_TYPE : [ ]* ('VILLAIN'[S]?':' | 'GUEST'[S]?':' | 'CAMEO'[S]?':' | 'CAMEO FLASHBACK'[S]?':' 
	| 'INTRODUCTION:' | 'FEATURE:' |'GUEST APPEARANCE:');
WS: [ \t\r\n]+ -> skip ;

fragment
BLANK: [ ]*;
EM_DASH: '<EM_DASH>'; 

		




