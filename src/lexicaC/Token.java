package lexicaC;

/**
 *
 * @author Thiagof
 */
public class Token {
    private String lexema;
    private NomeTokens classe;
    private int linha,coluna;
    
    public Token(NomeTokens classe, String lexema, int linha, int coluna){
        this.classe = classe;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getLexema() {
        return lexema;
    }

    public NomeTokens getClasse() {
        return classe;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setClasse(NomeTokens classe) {
        this.classe = classe;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
    
    @Override
    public String toString(){
        return "<" + classe + ", \"" + lexema + "\">";
    }
    
}
