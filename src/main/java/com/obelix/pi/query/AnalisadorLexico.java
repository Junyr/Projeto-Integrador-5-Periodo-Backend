package com.obelix.pi.query;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {

    private final String entrada;
    private final int tamanho;
    private int pos = 0;

    public AnalisadorLexico(String entrada) {
        this.entrada = entrada == null ? "" : entrada;
        this.tamanho = this.entrada.length();
    }

    public List<Token> tokenizar() {
        List<Token> tokens = new ArrayList<>();

        while (true) {
            pularEspacos();

            if (pos >= tamanho) {
                tokens.add(new Token(Token.Tipo.EOF, ""));
                break;
            }

            char c = peek();

            if (Character.isLetter(c) || c == '_') {
                String id = lerIdentificador();
                if ("AND".equalsIgnoreCase(id)) {
                    tokens.add(new Token(Token.Tipo.AND, id));
                } else {
                    tokens.add(new Token(Token.Tipo.IDENTIFICADOR, id));
                }
                continue;
            }

            if (c == '.') {
                pos++;
                tokens.add(new Token(Token.Tipo.PONTO, "."));
                continue;
            }

            if (c == '=') {
                pos++;
                tokens.add(new Token(Token.Tipo.OPERADOR, "="));
                continue;
            }

            if (c == '"') {
                String texto = lerTexto();
                tokens.add(new Token(Token.Tipo.TEXTO, texto));
                continue;
            }

            pos++;
        }

        return tokens;
    }

    private void pularEspacos() {
        while (pos < tamanho && Character.isWhitespace(entrada.charAt(pos))) pos++;
    }

    private char peek() {
        return entrada.charAt(pos);
    }

    private String lerIdentificador() {
        int inicio = pos;
        while (pos < tamanho && (Character.isLetterOrDigit(entrada.charAt(pos)) || entrada.charAt(pos) == '_'))
            pos++;
        return entrada.substring(inicio, pos);
    }

    private String lerTexto() {
        pos++; // pula "
        StringBuilder sb = new StringBuilder();

        while (pos < tamanho) {
            char c = entrada.charAt(pos++);
            if (c == '"') break;
            sb.append(c);
        }
        return sb.toString();
    }
}
