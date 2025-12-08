package com.obelix.pi.query;

public class Token {

    public enum Tipo {
        IDENTIFICADOR, PONTO, TEXTO, OPERADOR, AND, EOF
    }

    public final Tipo tipo;
    public final String lexema;

    public Token(Tipo tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    @Override
    public String toString() {
        return "[" + tipo + " \"" + lexema + "\"]";
    }
}