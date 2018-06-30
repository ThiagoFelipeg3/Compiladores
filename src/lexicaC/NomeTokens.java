package lexicaC;

/**
 *
 * @author Thiagof
 */
public enum NomeTokens {
    
    //Operadores
    OP_EQ,
    OP_NE,
    OP_GT,
    OP_LT,
    OP_GE,
    OP_LE,
    OP_AD,
    OP_MIN,
    OP_MUL,
    OP_DIV,
    OP_ASS,
    
    //Símbolos
    SMB_OBC,
    SMB_CBC,
    SMB_OPA,
    SMB_CPA,
    SMB_COM,
    SMB_SEM,
    
    //Palavra-chave
    KW_PROGRAM,
    KW_IF,
    KW_ELSE,
    KW_READ,
    KW_WRITE,
    KW_WHILE,
    KW_NOT,
    KW_AND,
    KW_OR,
    KW_NUM,
    KW_CHAR,
    
    
    //Identificadores
    ID,
    
    //Literal
    LIT,
    
    //Constantes
    CON_NUM,
    CON_CHAR,
    
    
    //Fim de arquivo
    EOF;
}
