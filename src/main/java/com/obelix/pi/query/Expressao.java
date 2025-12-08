package com.obelix.pi.query;

import java.util.ArrayList;
import java.util.List;

public class Expressao {

    public final List<Condicao> condicoes = new ArrayList<>();

    public void adicionar(Condicao condicao) {
        condicoes.add(condicao);
    }

    @Override
    public String toString() {
        if (condicoes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < condicoes.size(); i++) {
            if (i > 0) sb.append(" AND ");
            sb.append(condicoes.get(i));
        }
        return sb.toString();
    }
}
