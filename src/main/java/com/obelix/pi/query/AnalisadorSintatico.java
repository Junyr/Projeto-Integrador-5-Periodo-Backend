package com.obelix.pi.query;

import java.util.List;

/**
 * Parser da gramática:
 * EXPRESSAO → CONDICAO (AND CONDICAO)*
 * CONDICAO → IDENT . IDENT = "TEXTO"
 */
public class AnalisadorSintatico {

    private final List<Token> tokens;
    private int pos = 0;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token atual() {
        return tokens.get(pos);
    }

    private Token consumir() {
        return tokens.get(pos++);
    }

    public Expressao analisar() {
        Expressao expr = new Expressao();
        expr.adicionar(analisarCondicao());

        while (atual().tipo == Token.Tipo.AND) {
            consumir();
            expr.adicionar(analisarCondicao());
        }

        return expr;
    }

    private Condicao analisarCondicao() {
        if (atual().tipo != Token.Tipo.IDENTIFICADOR)
            throw new ErroSintaticoException("Esperado identificador. Recebido: " + atual());

        String entidade = consumir().lexema;

        if (atual().tipo != Token.Tipo.PONTO)
            throw new ErroSintaticoException("Esperado ponto '.' após identificador.");

        consumir();

        if (atual().tipo != Token.Tipo.IDENTIFICADOR)
            throw new ErroSintaticoException("Esperado nome de campo após '.'.");

        String campo = consumir().lexema;

        if (atual().tipo != Token.Tipo.OPERADOR)
            throw new ErroSintaticoException("Esperado operador '='.");

        consumir();

        if (atual().tipo != Token.Tipo.TEXTO)
            throw new ErroSintaticoException("Esperado literal entre aspas.");

        String valor = consumir().lexema;

        return new Condicao(entidade + "." + campo, valor);
    }
}