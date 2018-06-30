# Compiladores
Trabalho acadêmico de compiladores, Analise léxica, Analise Semântica e Analise Sintática. 

# Segue um exemplo da gramática PasC a Tabela Pretitiva o First e Follow

# Primeira etapa
Implementar um analisador léxico para a linguagem PasC.
Com o auxílio de um autômato finito determinístico. Ele deverá reconhecer um lexema e retornar, a cada chamada,
um objeto da classe Token, representando o token reconhecido de acordo com o lexema encontrado.

Resumindo, seu Analisador Léxico deverá imprimir a lista de todos os tokens reconhecidos, assim
como mostrar o que está cadastrado na Tabela de Símbolos. Na impressão dos tokens, deverá
aparecer a tupla <nome, lexema> assim como linha e coluna do token.

Além de reconhecer os tokens da linguagem, seu analisador léxico deverá detectar possíveis erros e
reportá-los ao usuário. O programa deverá informar o erro e o local onde ocorreu (linha e coluna),
lembrando que em análise léxica tem-se 3 tipos de erros: caracteres desconhecidos (não esperados
ou inválidos), string não-fechada antes de quebra de linha e comentário não-fechado antes do fim de
arquivo. 
Espaços em branco, tabulações, quebras de linhas e comentários não são tokens, ou seja, devem ser
descartados/ignorados pelo referido analisador.

# Segunda etapa
Implementar  um analisador sintático  descendente  (top-down)  para a linguagem PasC.
Ele deverá interagir com  o  analisador  léxico  para  obter  os  tokens  do  arquivo-fonte.  Você  deve  implementar  seu
analisador sintático utilizando o algoritmo de Parser Preditivo Recursivo (Procedimentos para cada
Não-terminal) 

O analisador sintático deverá reportar possíveis erros ocorridos no programa-fonte. O analisador
deverá informar qual o erro encontrado (informar que token era espearado e qual token apareceu) e
sua localização no arquivo-fonte. 
