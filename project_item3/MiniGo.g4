grammar MiniGo;

program     : decl+ ; 
decl        : var_decl
            | fun_decl ;
var_decl    : VAR IDENT type_spec
            | VAR IDENT ',' IDENT type_spec
            | VAR IDENT '[' LITERAL ']' type_spec ;
type_spec   : INT 
            | VOID 
            | ; 
fun_decl    : FUNC IDENT '(' params ')' type_spec compound_stmt  
            | FUNC IDENT '(' params ')' '(' type_spec ',' type_spec ')' compound_stmt;
params      :  
            | param(',' param)* ;
param       : IDENT type_spec 
            | IDENT '[' ']' type_spec ;
stmt        : expr_stmt
            | compound_stmt
            | assign_stmt
            | if_stmt
            | for_stmt
            | return_stmt;
expr_stmt   : expr ;
assign_stmt : VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
            | VAR IDENT type_spec '=' expr
            | IDENT type_spec '=' expr
            | IDENT '[' expr ']' '=' expr ;
compound_stmt: '{' local_decl* stmt* '}';
if_stmt     : IF expr compound_stmt
            | IF expr compound_stmt ELSE compound_stmt ;
for_stmt    : FOR expr compound_stmt;
return_stmt : RETURN expr ',' expr
            | RETURN expr
            | RETURN ;
local_decl  : VAR IDENT type_spec
            | VAR IDENT '[' LITERAL ']' type_spec;
expr        : IDENT '(' args ')'
            | (LITERAL|IDENT)
            | '(' expr ')' 
            | IDENT '[' expr ']' 
            | FMT '.' IDENT '(' args ')' 
            | op=('-'|'+'|'--'|'++'|'!') expr 
            | left=expr op=('*'|'/'|'%') right=expr 
            | left=expr op=('+'|'-') right=expr 
            | left=expr op=(EQ|NE|LE|'<'|GE|'>'|AND|OR) right=expr
            | LITERAL ',' LITERAL
            | IDENT '=' expr
            | IDENT '[' expr ']' '=' expr;
args        : expr (',' expr)* 
            | ;
         
VOID        : 'void'  ;
VAR         : 'var'   ;
FUNC        : 'func'  ;
FMT         : 'fmt'   ;
INT         : 'int'   ;
FOR         : 'for'   ;
IF          : 'if'    ;
ELSE        : 'else'  ;
RETURN      : 'return';
OR          : 'or'    ;
AND         : 'and'   ;
LE          : '<='    ;
GE          : '>='    ;
EQ          : '=='    ;
NE          : '!='    ;

IDENT       : [a-zA-Z_] 
            ( [a-zA-Z_]
            | [0-9]
            )*;
         
LITERAL     : DecimalConstant | OctalConstant | HexadecimalConstant ;

DecimalConstant : '0' | [1-9] [0-9]* ;
OctalConstant   : '0' [0-7]* ;
HexadecimalConstant : '0' [xX] [0-9a-fA-F]+ ;
WS       : (' '
         | '\t'
         | '\r'
         | '\n'        
         )+
   -> channel(HIDDEN)  
    ;