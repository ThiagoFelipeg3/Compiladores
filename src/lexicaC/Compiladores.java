package lexicaC;

/**
 *
 * @author Thiago Felipe P. de Oliveira
 * RA: 11516023
 * Este codigo foi implementado sem o modo panico.
 */
public class Compiladores {
     
    public static void main(String[]args){
        Automato aut = new Automato("correto1.jvn");
        Parser parser = new Parser(aut);


         parser.prog();

          parser.fechaArquivos();

          //Imprimir a tabela de simbolos
          aut.printTS();

          System.out.println("Compilação de Programa Realizada!");

    }
   
}
