package lexicaC;

import java.util.HashMap;

/**
 *
 * @author Thiagof
 */
public class TabelaS {
     private HashMap<Token, InfIdentificador> tabelaSimbolos; // Tabela de símbolos do ambiente

    public TabelaS() {
        tabelaSimbolos = new HashMap();

        // Inserindo as palavras reservadas
        Token word;
        word = new Token(NomeTokens.KW_PROGRAM, "program", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_IF, "if", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_ELSE, "else", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_WHILE, "while", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_WRITE, "write", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_READ, "read", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_NUM, "num", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_CHAR, "char", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_NOT, "not", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_OR, "or", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
        
        word = new Token(NomeTokens.KW_AND, "and", 0, 0);
        this.tabelaSimbolos.put(word, new InfIdentificador());
    
    
    }
    
    public void put(Token w, InfIdentificador i) {
        tabelaSimbolos.put(w, i);
    }

    // Retorna um identificador de um determinado token
    public InfIdentificador getIdentificador(Token w) {
        InfIdentificador infoIdentificador = (InfIdentificador) tabelaSimbolos.get(w);
        return infoIdentificador;
    }

    // Pesquisa na tabela de símbolos se há algum tokem com determinado lexema
    // vamos usar esse metodo somente para diferenciar ID e KW
    public Token retornaToken(String lexema) {
        for (Token token : tabelaSimbolos.keySet()) {
            if (token.getLexema().equals(lexema)) {
                return token;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String saida = "";
        int i = 1;
        for (Token token : tabelaSimbolos.keySet()) {
            saida += ("posicao " + i + ": \t" + token.toString()) + "\n";
            i++;
        }
        return saida;
    }
}
