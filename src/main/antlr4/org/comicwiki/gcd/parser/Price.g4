grammar Price;   
@header {
//    package org.comicwiki.gcd.parser;
}  

priceField
  :  (price | fractionPrice) inferredPrice? NOTE?
  ;

fractionPrice
	: FRACTION (LEFT_BRACKET BRITISH RIGHT_BRACKET)?
	;
	
price
	: PRICE CURRENCY_CODE COUNTRY? 
	;

inferredPrice 
	: LEFT_BRACKET price RIGHT_BRACKET
	;	
	
CURRENCY_CODE
		: [A-Z] [A-Z] [A-Z]
		;
COUNTRY
	: [A-Z] [A-Z]
	;
	
ALPHA
    :  [a-zA-Z]
    ;
    
NOTE
	: LEFT_PAREN .*? RIGHT_PAREN
	{ setText(getText().substring(1, getText().length() - 1 ).trim());}
	;
	    
PRICE
	: DECIMAL_NUMBER
  	| INTEGER_NUMBER
	;

BRITISH
	: INTEGER_NUMBER '-' INTEGER_NUMBER '-' INTEGER_NUMBER 
	;
	
FRACTION 
	: INTEGER_NUMBER '/' INTEGER_NUMBER
	;
		
DIGIT   
	:   ('0'..'9')
	;
	 
DELIM 
	: (';' | ',')
	;
	   
LEFT_BRACKET
	: '['
	;
	
RIGHT_BRACKET
	: ']'
	;
LEFT_PAREN
	: '(' 
	;
RIGHT_PAREN
	: ')' 
	;
	
DECIMAL_NUMBER
  :  INTEGER_NUMBER ('.' |','|':') INTEGER_NUMBER
  |  ('.') INTEGER_NUMBER
  ;

INTEGER_NUMBER  
  :  '0'..'9'+
  ;

WS 
  :  (' ' | '\t' | '\f' | '\r' | '\n')+ -> skip
  ;