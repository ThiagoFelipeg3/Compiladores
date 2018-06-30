package lexicaC;

/**
 *
 * @author Thiagof
 * O parse foi feito sem o modo panico
 */
public class Parser {
    
    private final Automato aut;
    private Token token;

    public Parser(Automato aut) {
        this.aut = aut;
        token = aut.getToken(); // Leitura inicial obrigatoria do primeiro simbolo
        System.out.println("[DEBUG]" + token.toString()+" linha: " + token.getLinha() + " coluna:" + token.getColuna());
    }

    // Fecha os arquivos de entrada e de tokens
    public void fechaArquivos() {

        aut.fechaArquivo();
    }

    public void erroSintatico(String mensagem) {

        System.out.println("[Erro Sintatico] na linha " + token.getLinha() + " e coluna " + token.getColuna());
        System.out.println(mensagem + "\n");
    }
    
    public void advance() {
    	token = aut.getToken();
    	System.out.println("[DEBUG]" + token.toString()+" linha:"+token.getLinha()+" coluna:"+ token.getColuna());
    }
    
	// verifica token esperado t
   public boolean eat(NomeTokens t) {
		if(token.getClasse() == t) {
			advance();
			return true;
		} 
		else {
			return false;
		}
   }
   // prog  → “program” “id” body 1
    public void prog(){
       
        if(eat(NomeTokens.KW_PROGRAM)){
            if(!eat(NomeTokens.ID)){
                erroSintatico("Esperado um \"ID\", encontrado " + token.getLexema());
                System.exit(1);
            }
            body();
        }else{
            erroSintatico("Esperado um \"program\", encontrado " + token.getLexema());
      	    System.exit(1);
        }
        
    }
    // body  → decl-list “{“ stmt-list “}” 2
    public void body(){
        
        decl_list();
        
        if(eat(NomeTokens.SMB_OBC)){ //Esperado {
            stmt_list();
            if(!eat(NomeTokens.SMB_CBC)){
                erroSintatico("Esperado um \"}\", encontrado " + token.getLexema());
                System.exit(1);
            }
        }else{
            erroSintatico("Esperado um \"{\", encontrado " + token.getLexema());
      	    System.exit(1);
            
        }
    }
    // decl-list  → decl “;” decl-list 3|  ε 4
    public void decl_list(){
        
        if(token.getClasse() == NomeTokens.KW_NUM || token.getClasse() == NomeTokens.KW_CHAR){
           decl();

            if(!eat(NomeTokens.SMB_SEM)){
                erroSintatico("Esperado um \";\", encontrado " + token.getLexema());
                System.exit(1);
            
                decl_list();
            }
        }else if(token.getClasse() == NomeTokens.SMB_CBC){
                    return; 
        }else{
                erroSintatico("Esperado um \"NUM ou CHAR\", encontrado " + token.getLexema());
                    System.exit(1);
        } 
           
    }
    // decl  → type id-list 5
    public void decl(){
        type();
        id_list();
    }
    
    // type  → “num” 6 | “char” 7
    public void type(){
        if(!eat(NomeTokens.KW_NUM) && !eat(NomeTokens.KW_CHAR)  ){
            erroSintatico("Esperado um \"NUM ou CHAR\", encontrado " + token.getLexema());
                System.exit(1);
        }
    }
    // id-list  → “id” id-list’ 8
    public void id_list(){
         
        if(eat(NomeTokens.ID)){
            id_listLinha();
           
        }else{
            erroSintatico("Esperado um \"ID\", encontrado " + token.getLexema());
            System.exit(1);
            
        }
    }
    // id-list’ → “,” id-list 9  | ε 10
    public void id_listLinha(){
        
        if(token.getClasse() == NomeTokens.SMB_COM){// Espera ,
            if(eat(NomeTokens.SMB_COM)){
               id_list(); 
            }
        }else if(token.getClasse() == NomeTokens.SMB_SEM){
            return;
        }else{
            erroSintatico("Esperado um \", ou ;\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    
    // stmt-list  → stmt “;” stmt-list 11| ε 12
    public void stmt_list(){
        //“id”, “if”, “while”, “read”, “write”, “ε”
      if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.KW_IF || token.getClasse() == NomeTokens.KW_WHILE || token.getClasse() == NomeTokens.KW_READ || token.getClasse() == NomeTokens.KW_WRITE){
          stmt();
          
         if(!eat(NomeTokens.SMB_SEM)){//Esperava ;
            erroSintatico("Esperado um \";\", encontrado " + token.getLexema());
            System.exit(1);
          }
          stmt_list();
      }else if(token.getClasse() == NomeTokens.SMB_CBC){
          return;
      }else{
           erroSintatico("Esperado um \"id, if, while, read, write\", encontrado " + token.getLexema());
            System.exit(1);
      }    
    }
    // stmt  → assign-stmt 13| if-stmt 14| while-stmt 15| read-stmt 16| write-stmt 17
    public void stmt(){
        if(token.getClasse() == NomeTokens.ID){
            assign_stmt();
        
        }else if(token.getClasse() == NomeTokens.KW_IF){
                if_stmt();
        
        }else if(token.getClasse() == NomeTokens.KW_WHILE){
                while_stmt();
        
        }else if(token.getClasse() == NomeTokens.KW_READ){
                read_stmt();
        
        }else if(token.getClasse() == NomeTokens.KW_WRITE){
                write_stmt();
        }else{
            erroSintatico("Esperado um \"id, if, while, read, write\", encontrado " + token.getLexema());
            System.exit(1);
            
        }
    }
    // assign-stmt  → “id” “=” simple_expr 18
    public void assign_stmt(){
        
        if(eat(NomeTokens.ID)){
            if(!eat(NomeTokens.OP_ASS)){
                erroSintatico("Esperado um \"=\", encontrado " + token.getLexema());
                System.exit(1);
            }
            simple_expr();
        }else{
            erroSintatico("Esperado um \"id\", encontrado " + token.getLexema());
            System.exit(1);
        }
        
    }
    // if-stmt  → “if” “(“ condition “)” “{“ stmt-list “}” if-stmt’ 19
    public void if_stmt(){
        if(eat(NomeTokens.KW_IF)){ // Esperado if
            if(!eat(NomeTokens.SMB_OPA)){ //Esperado (
                erroSintatico("Esperado um \"(\", encontrado " + token.getLexema());
                System.exit(1);
            }
            condition();
            if(!eat(NomeTokens.SMB_CPA)){ // Esperado )
                erroSintatico("Esperado um \")\", encontrado " + token.getLexema());
                System.exit(1);
            }
            if(!eat(NomeTokens.SMB_OBC)){// Esperado {
                erroSintatico("Esperado um \"{\", encontrado " + token.getLexema());
                System.exit(1);
            }
            stmt_list();
            if(!eat(NomeTokens.SMB_CBC)){//Esperado }
                erroSintatico("Esperado um \"}\", encontrado " + token.getLexema());
                System.exit(1);
            }
            if_stmtLinha();
        }else{
            erroSintatico("Esperado um \"if\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // if-stmt’ → “else” “{“ stmt-list “}”  20 | ε 21
    public void if_stmtLinha(){
        if(eat(NomeTokens.KW_ELSE)){// Esperado else
            
            if(!eat(NomeTokens.SMB_OBC)){// Esperado {
                erroSintatico("Esperado um \"{\", encontrado " + token.getLexema());
                System.exit(1);
            }
            stmt_list();
            
            if(!eat(NomeTokens.SMB_CBC)){//Esperado }
                erroSintatico("Esperado um \"}\", encontrado " + token.getLexema());
                System.exit(1);
            }
        }else if(token.getClasse() == NomeTokens.SMB_SEM){
            return;
            
        }else{
            erroSintatico("Esperado um \";\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // condition  → expression 22
    public void condition(){
        if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR || token.getClasse() == NomeTokens.SMB_OPA || token.getClasse() == NomeTokens.KW_NOT){
            expression();
        }else{
            erroSintatico("Esperado um \"id, num_const, char_const, (, not\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // while-stmt  → stmt-prefix “{“ stmt-list “}” 23
    public void while_stmt(){
        if(token.getClasse() == NomeTokens.KW_WHILE){// Esperado while
            stmt_prefix();
            
            if(!eat(NomeTokens.SMB_OBC)){// Esperado {
                erroSintatico("Esperado um \"{\", encontrado " + token.getLexema());
                System.exit(1);
            }
            stmt_list();
            
            if(!eat(NomeTokens.SMB_CBC)){//Esperado }
                erroSintatico("Esperado um \"}\", encontrado " + token.getLexema());
                System.exit(1);
            }
        }else{
            erroSintatico("Esperado um \"while\", encontrado " + token.getLexema());
            System.exit(1);
            
        }
    }
    // stmt-prefix  → “while” “(“ condition “)” 24
    public void stmt_prefix(){
        if(eat(NomeTokens.KW_WHILE)){// Esperado while
            if(!eat(NomeTokens.SMB_OPA)){ //Esperado (
                erroSintatico("Esperado um \"(\", encontrado " + token.getLexema());
                System.exit(1);
            }
            condition();
            if(!eat(NomeTokens.SMB_CPA)){ // Esperado )
                erroSintatico("Esperado um \")\", encontrado " + token.getLexema());
                System.exit(1);
            }
        }else{
             erroSintatico("Esperado um \"while\", encontrado " + token.getLexema());
            System.exit(1);  
        }
    }
    // read-stmt  → “read” “id” 25
    public void read_stmt(){
        if(eat(NomeTokens.KW_READ)){// Esperado read
            if(!eat(NomeTokens.ID)){
                erroSintatico("Esperado um \"id\", encontrado " + token.getLexema());
                System.exit(1);
            }
        }else{
            erroSintatico("Esperado um \"read\", encontrado " + token.getLexema());
            System.exit(1);
        }
        
    }
    // write-stmt  → “write” writable 26
    public void write_stmt(){
        if(eat(NomeTokens.KW_WRITE)){// Esperado write
            
            //“id”, “num_const”, “char_const”, “(“,“not”, “literal”
            if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR || token.getClasse() == NomeTokens.SMB_OPA || token.getClasse() == NomeTokens.KW_NOT || token.getClasse() == NomeTokens.LIT){
                writable();
            }else{
                 erroSintatico("Esperado um \"id, num_const, char_const, (,not, literal\", encontrado " + token.getLexema());
                 System.exit(1);
            }
           
        }else{
            erroSintatico("Esperado um \"read\", encontrado " + token.getLexema());
            System.exit(1);
        }
        
    }
    // writable  → simple-expr 27| “literal” 28
    public void writable(){
       
        if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR || token.getClasse() == NomeTokens.SMB_OPA || token.getClasse() == NomeTokens.KW_NOT){
            simple_expr();
        }else if(!eat(NomeTokens.LIT)){
            erroSintatico("Esperado um \"literal, id, con_num, con_char, not, ( \", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // expression  → simple-expr expression’ 29
    public void expression(){
        
         if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR || token.getClasse() == NomeTokens.SMB_OPA || token.getClasse() == NomeTokens.KW_NOT){
            simple_expr();
            expressionLinha();
         }else{
             erroSintatico("Esperado um \"id, num_const, char_const, (,not\", encontrado " + token.getLexema());
             System.exit(1);
         }
           
    }
    // expression’ → relop simple-expr 30 | ε 31
    public void expressionLinha(){
        // “==”, “>”, “>=”, “<”, “<=”, “!=”, “ε”
        if(token.getClasse() == NomeTokens.OP_EQ 
                || token.getClasse() == NomeTokens.OP_GT
                || token.getClasse() == NomeTokens.OP_GE 
                || token.getClasse() == NomeTokens.OP_LT
                || token.getClasse() == NomeTokens.OP_LE
                || token.getClasse() == NomeTokens.OP_NE){
            relop();
            simple_expr();
        }else if(token.getClasse() == NomeTokens.SMB_CPA){
            return;
        }else{
            erroSintatico("Esperado um \"==, >, >=, <, <=, !=, ε\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // simple-expr  → term simple-expr’  32
    public void simple_expr(){
        term();
        simple_exprLinha();
    }
    
    // simple-expr’ → addop term simple-expr’  33  | ε 34
    public void simple_exprLinha(){
        // “+”, “-”, “or” 
        if(token.getClasse() == NomeTokens.OP_AD || token.getClasse() == NomeTokens.OP_MIN || token.getClasse() == NomeTokens.KW_OR){
            addop();
            term();
            simple_exprLinha();
        
                // “;”, “==”, “>”, “>=”, “<”, “<=”, “!=”, “)” follow
        }else if(token.getClasse() == NomeTokens.SMB_SEM 
                || token.getClasse() == NomeTokens.OP_EQ 
                || token.getClasse() == NomeTokens.OP_GT
                || token.getClasse() == NomeTokens.OP_GE 
                || token.getClasse() == NomeTokens.OP_LT
                || token.getClasse() == NomeTokens.OP_LE
                || token.getClasse() == NomeTokens.OP_NE
                || token.getClasse() == NomeTokens.SMB_CPA){
            return;
        }
        
    }
    // term  → factor-a term’ 35
    public void term(){
        factor_a();
        termLinha();
    
    }
    // term’ → mulop factor-a term’ 36 | ε 37
    public void termLinha(){
        // “*”, “/”, “and”, “ε”
        if(token.getClasse() == NomeTokens.OP_MUL || token.getClasse() == NomeTokens.OP_DIV || token.getClasse() == NomeTokens.KW_AND){
            mulop();
            factor_a();
            termLinha();
            //“+”, “-”, “or”, “;”, “==”, “>”, “>=”, “<”, “<=”, “!=”, “)”
        }else if(token.getClasse() == NomeTokens.OP_AD 
                || token.getClasse() == NomeTokens.OP_MIN 
                || token.getClasse() == NomeTokens.KW_OR
                || token.getClasse() == NomeTokens.SMB_SEM 
                || token.getClasse() == NomeTokens.OP_EQ 
                || token.getClasse() == NomeTokens.OP_GT
                || token.getClasse() == NomeTokens.OP_GE 
                || token.getClasse() == NomeTokens.OP_LT
                || token.getClasse() == NomeTokens.OP_LE
                || token.getClasse() == NomeTokens.OP_NE
                || token.getClasse() == NomeTokens.SMB_CPA){
            return;
        }else{
            erroSintatico("Esperado um \"*, /, and ou +, -, or, ;, ==, >, >=, <, <=, !=, )\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // factor-a  → factor 38| “not” factor 39
    public void factor_a(){
        // “id”, “num_const”, “char_const”, “(“
        if(token.getClasse() == NomeTokens.ID || token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR || token.getClasse() == NomeTokens.SMB_CPA){
            factor();
        }else if(token.getClasse() == NomeTokens.KW_NOT){
            if(eat(NomeTokens.KW_NOT)){
                factor();
            }
        }else{
            erroSintatico("Esperado um \"id, num_const, char_const, (, not\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // factor  → “id” 40| constant 41| “(“ expression “)” 42
    public void factor(){
        if(token.getClasse() == NomeTokens.ID){
            eat(NomeTokens.ID);
            
        }else if(token.getClasse() == NomeTokens.CON_NUM || token.getClasse() == NomeTokens.CON_CHAR){
            constant();
        
        }else if(eat(NomeTokens.SMB_OPA)){
            expression();
            
            if(!eat(NomeTokens.SMB_CPA)){
                erroSintatico("Esperado um \")\", encontrado " + token.getLexema());
                System.exit(1);
            }
            
        }else{
            erroSintatico("Esperado um \"id, con_num, con_char, )\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // relop  → “==”43| “>” 44| “>=” 45| “<” 46| “<=” 47| “!=” 48
    public void relop(){
        
        if(!eat(NomeTokens.OP_EQ) && !eat(NomeTokens.OP_GT) && !eat(NomeTokens.OP_GE) && !eat(NomeTokens.OP_LT) && !eat(NomeTokens.OP_LE) && !eat(NomeTokens.OP_NE)){
            
            erroSintatico("Esperado um \"==, >, >=, <, <=, !=\", encontrado " + token.getLexema());
            System.exit(1);
        }
    }
    // addop  → “+” 49| “-” 50| “or” 51
    public void addop(){
      
        if(!eat(NomeTokens.OP_AD) && !eat(NomeTokens.OP_MIN) && !eat(NomeTokens.KW_OR)){
          erroSintatico("Esperado um \"+, -, or\", encontrado " + token.getLexema());
          System.exit(1);
      }
        
    }
    // mulop  → “*” 52| “/”53| “and” 54
    public void mulop(){
      if(!eat(NomeTokens.OP_MUL) && !eat(NomeTokens.OP_DIV) && !eat(NomeTokens.KW_AND)){
          erroSintatico("Esperado um \"*, /, and\", encontrado " + token.getLexema());
          System.exit(1);
      }   
        
    }
    // constant  → “num_const” 55| “char_const” 56
    public void constant(){
       
        if(!eat(NomeTokens.CON_NUM) && !eat(NomeTokens.CON_CHAR)){
           erroSintatico("Esperado um \" CON_NUM ou CON_CHAR\", encontrado " + token.getLexema());
            System.exit(1); 
        }
        
    }
}
