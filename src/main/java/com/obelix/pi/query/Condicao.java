package com.obelix.pi.query;

public class Condicao {

    public final String identificador;
    public final String valor;

    public Condicao(String identificador, String valor) {
        this.identificador = identificador;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return identificador + " = \"" + valor + "\"";
    }
}
