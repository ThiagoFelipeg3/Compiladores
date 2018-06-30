package lexicaC;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Thiagof
 */
public class Automato {
    
       
    private static final int END_OF_FILE = -1; // contante para fim do arquivo
    private static int lookahead = 0; // armazena o último caractere lido do arquivo	
    public static int numero_linha = 1; // contador de linhas
    public static int numero_coluna = 1; // contador de colunas
    private RandomAccessFile instance_file; // referencia para o arquivo
    private static TabelaS tabelaSimbolos; // tabela de simbolos
    
    public Automato(String input_data) {
		
        // Abre instance_file de input_data
	try {
            instance_file = new RandomAccessFile(input_data, "r");
	}
	catch(IOException e) {
            System.out.println("Erro de abertura do arquivo " + input_data + "\n" + e);
            System.exit(1);
	}
	catch(Exception e) {
            System.out.println("Erro do programa ou falha da tabela de simbolos\n" + e);
            System.exit(2);
	}
        tabelaSimbolos = new TabelaS(); // tabela de simbolos
    }
    
    public void printTS() {
		System.out.println("");
      System.out.println("--------Tabela de Simbolos--------");
      System.out.println(tabelaSimbolos.toString());  
      System.out.println();
   }
    // Fecha instance_file de input_data
    public void fechaArquivo() {

        try {
            instance_file.close();
        }
	catch (IOException errorFile) {
            System.out.println ("Erro ao fechar arquivo\n" + errorFile);
            System.exit(3);
	}
    }
    
    //Reporta erro para o usuário
    public void sinalizaErro(String mensagem) {
        System.out.println("[Erro Lexico]: " + mensagem + "\n");
    }
    
    //Volta uma posição do buffer de leitura
    public void retornaPonteiro(){
            
        try {
            // Não é necessário retornar o ponteiro em caso de Fim de Arquivo
            if (lookahead != END_OF_FILE) {
                instance_file.seek(instance_file.getFilePointer() - 1);
            } 
            numero_coluna--;
        }
        catch(IOException e) {
            System.out.println("Falha ao retornar a leitura\n" + e);
            System.exit(4);
        }
    }
   /*******************************
    * Este metodo foi feito para 
    * força o avanço do ponteiro
    * por exemplo o tratamento de
    * comentarios.
    *******************************/
   public char avancaCaractere(){
        char c;
        c = '\u0000';
        // avanca caractere
            try {
                lookahead = instance_file.read(); 
		if(lookahead != END_OF_FILE) {
                    c = (char) lookahead;        //Convertendo um inteiro para um char e atribuindo a variavel.
                    numero_coluna++;
                }
            }
            catch(IOException e) {
                System.out.println("Erro na leitura do arquivo");
                System.exit(3);
            }
            
            return c;
            
    }
    public Token getToken() {

	StringBuilder lexema = new StringBuilder();
	int estado = 1;
	char c;
        String msg = " ";
		
	while(true) {
            c = avancaCaractere();
            
           
            // movimentacao do automato
            switch(estado) {
                case 1:
                    if (lookahead == END_OF_FILE)  //Receonhece final de arquivo 
                        return new Token(NomeTokens.EOF, "EOF", numero_linha, numero_coluna);
                    else if (c == ' ' || c == '\t' || c == '\n') {
                        // Permance no estado = 1
                        if(c == '\n'){
                            numero_linha++;
                            numero_coluna = 1;
                        }
                        else if(c == '\t') {
                           numero_coluna += 2;
                       }
                        
                    }
                    else if (Character.isLetter(c)){  // Classe Character, métodos de apoio ao tratamento de caracteres.
                        lexema.append(c);             // vai acrescentar no lexema o char c   
                        estado = 2;                  // Se reconhecer uma letra vai para o estado 14
                    }                                 // Se reconhecer um digito vai para o estado 12
                    else if (Character.isDigit(c)) {
                        lexema.append(c);
                        estado = 3;
                    }
                    else if (c == '<') {
                        estado = 4;
                    }
                    else if (c == '>') {
                        estado = 5;
                    }
                    else if (c == '=') {
                        estado = 6;
                    }
                    else if (c == '!') {
                        estado = 7;
                    }
                    else if (c == '/') {
                        estado = 8;
                    }
                    else if(c == '"'){
                        estado = 9;
                    }
                    else if(c == '\''){
                        estado = 10;
                    }
                    else if (c == '*') {
                        return new Token(NomeTokens.OP_MUL, "*", numero_linha, numero_coluna);
                    }
                    else if(c == '+') {
                        return new Token(NomeTokens.OP_AD, "+", numero_linha, numero_coluna);
                    }
                    else if(c == '-') {
                        return new Token(NomeTokens.OP_MIN, "-", numero_linha, numero_coluna);
                    }
                    else if(c == ';') {
                        return new Token(NomeTokens.SMB_SEM, ";", numero_linha, numero_coluna);
                    }
                    else if(c == '(') {
                        return new Token(NomeTokens.SMB_OPA, "(", numero_linha, numero_coluna);
                    }
                    else if(c == ')') {
                        return new Token(NomeTokens.SMB_CPA, ")", numero_linha, numero_coluna);
                    }
                    else if(c == '{') {
                        return new Token(NomeTokens.SMB_OBC, "{", numero_linha, numero_coluna);
                    }
                    else if(c == '}') {
                        return new Token(NomeTokens.SMB_CBC, "}", numero_linha, numero_coluna);
                    }
                    else if(c == ',') {
                        return new Token(NomeTokens.SMB_COM, ",", numero_linha, numero_coluna);
                    }
                    else{
                        sinalizaErro("Caractere invalido " + c +" na linha " + numero_linha + " e coluna " + numero_coluna);
                        estado = 1;
                    }
                    break;
                case 2:
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        lexema.append(c);
			// Permanece no estado 2
                    }
                    else { 
                        estado = 1;
                        
			retornaPonteiro();  
                        Token token = tabelaSimbolos.retornaToken(lexema.toString());
                        
                        if (token == null) {
                            return new Token(NomeTokens.ID, lexema.toString(), numero_linha, numero_coluna);
                        }
                        token.setLinha(numero_linha);
                        token.setColuna(numero_coluna);
                        return token;
                    }
                    break;
                case 3:
                    if (Character.isDigit(c)) {
                         lexema.append(c);
                        // Permanece no estado 3
                    }else if(c == '.'){
                        lexema.append(c);
                         estado = 300; 
                    }
                    else { 
                        estado = 1;
                        
                        retornaPonteiro();						
			return new Token(NomeTokens.CON_NUM, lexema.toString(), numero_linha, numero_coluna);
                    }
                    break;                    
                    case 300:
                    if(Character.isDigit(c)){
                        lexema.append(c);
                        
                         
                    }   
                    else{
                        estado = 1;
                        
                        retornaPonteiro();						
			return new Token(NomeTokens.CON_NUM, lexema.toString(), numero_linha, numero_coluna);
                        
                    }
                    break;
                    
                case 4:
                    if (c == '=') {
                        estado = 1;
			return new Token(NomeTokens.OP_LE, "<=", numero_linha, numero_coluna);
                    }
                    else { 
                        estado = 1;
			retornaPonteiro();
                        
			return new Token(NomeTokens.OP_LT, "<", numero_linha, numero_coluna);
                    }
                    
                // > || >=
                case 5:
                    if (c == '=') { 
                        estado = 1;
                        return new Token(NomeTokens.OP_GE, ">=", numero_linha, numero_coluna);
                    }
                    else { 
                        estado = 1;
                        retornaPonteiro();
                        
                        return new Token(NomeTokens.OP_GT, ">", numero_linha, numero_coluna);
                    }
                // = || ==    
                case 6:
                    if (c == '=') { 
                        estado = 1;
                        return new Token(NomeTokens.OP_EQ, "==", numero_linha, numero_coluna);
                    }
                    else {
                        retornaPonteiro();
                        return new Token(NomeTokens.OP_ASS, "=", numero_linha, numero_coluna);
                    }
                // !=    
		case 7:
                    if (c == '=') { 
                        estado = 1;
			return new Token(NomeTokens.OP_NE, "!=", numero_linha, numero_coluna);
                    }
                    else {
                        retornaPonteiro();
                        sinalizaErro("Token incompleto para o caractere ! na linha " + numero_linha + " e coluna " + numero_coluna);
			return null;
                    }
                // / || // || /*  Tratamento de comentários e do operador OP_DIV /  
                case 8:
                    if (c == '/') {   //Comentário de uma linha se for duas barras ele entra no case 90
                        estado = 90;   
                    }
                    else if(c == '*'){ /*Comentário com mais de uma linhas se for ele entra no case 100*/
                        estado = 100;
                    }
                    else {
                        estado = 1;
                        retornaPonteiro();
			return new Token(NomeTokens.OP_DIV, "/", numero_linha, numero_coluna);
                    }
                    break;
                    
                    case 90:
                    if (c == '\n') {    //Se tiver alguma quebra de linha por ser um comentario com mais de duas linha
                        estado = 1;
                       
                       System.out.printf("Comentario: "+ msg + " linha: "+numero_linha+" coluna: "+numero_coluna+ "\n");
                       msg = " ";
                       numero_coluna = 1;
                    }
                    else{
                        msg += c;  //Serve para salvar o comentário para poder imprimir depois  
                    }    
                    break;
                   
                    case 100:
                    if(c == '*'){
                        c = avancaCaractere(); // força o avanço do ponteiro
                        
                        if(c == '/'){ //O proximo esperado é uma barra / caso não for da erro pois ele não esta fechado corretamente 
                            
                            System.out.printf("Comentário: "+ msg + " linha: "+numero_linha+" coluna: "+numero_coluna+"\n");
                            estado = 1;
                        }else{
                            msg = "Comentario não esta fechado corretamente";
                            sinalizaErro(msg);
                            estado = 1;
                        }
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("O comentário deve ser fechado com */ antes do fim de arquivo linha: "+numero_linha+" coluna: "+numero_coluna);
			return null;
                    }
                    else if(c == '\n'){
                        numero_linha++;
                    }
                    else{
                        msg += c;
                    }
                   
                    break;
                case 9:
                    if (c == '"') { //Se for um literal
                        estado = 1;
                        return new Token(NomeTokens.LIT, lexema.toString(), numero_linha, numero_coluna);
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("Literal deve ser fechada com \" antes do fim de arquivo linha: "+numero_linha+" coluna: "+numero_coluna);
			return null;
                    }
                    else { 
                        lexema.append(c);
                    }
                    break;    
                //Testendo se é um CON_CHAR obs: tem de ser um caracter
                case 10:
                    
                    //Testando se tem somente um caracter 
                    if ((avancaCaractere()) == '\'') { 
                        lexema.append(c);
                        estado = 1;
                        return new Token(NomeTokens.CON_CHAR, lexema.toString(), numero_linha, numero_coluna);
                    }
                    //Este teste verifica se o proximo caracter é diferente de aspas simples 
                    //Caso for ele retorna um erro pois o CNO_CHAR deve ser 
                    //um caracter fechar com '
                    else if((c = avancaCaractere()) != '\''){
                        sinalizaErro("CON_CHAR deve ser fechada com \' e ter somente um caracter. linha: "+numero_linha+" coluna: "+numero_coluna);
			estado = 1;
                    }
                     break;
                default:
                    estado = 1;
                    
                    break;
            }
            
        }
    }

    /*****************************
     * Este era o modelo antigo 
     * do primeiro trabalho, e nao 
     * funciona mais.
     ****************************/
    /*public static void main(String[] args) {
        Automato lexer = new Automato("correto1.jvn"); 
	Token token;
        tabelaSimbolos = new TabelaS();

	// Enquanto não houver erros ou não for fim de arquivo:
	do {
            token = lexer.proxToken();
            
            // Imprime token
	    if(token != null) {
                System.out.println("Token: " + token.toString() + "\t Linha: " + numero_linha + "\t Coluna: " + numero_coluna);
                
                // Verificar se existe o lexema na tabela de símbolos
                
            }
	     
	} while(token != null && token.getClasse() != NomeTokens.EOF);
	lexer.fechaArquivo();
        
    }*/
    
}
