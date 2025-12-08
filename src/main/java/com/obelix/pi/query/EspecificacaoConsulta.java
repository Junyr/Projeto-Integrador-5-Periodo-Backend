package com.obelix.pi.query;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.*;

public class EspecificacaoConsulta<T> {

    public Specification<T> paraEspecificacao(Expressao expressao) {

        return (root, query, cb) -> {

            List<Predicate> predicados = new ArrayList<>();

            for (Condicao c : expressao.condicoes) {
                String[] partes = c.identificador.split("\\.");

                if (partes.length == 1) {
                    predicados.add(cb.equal(root.get(partes[0]), c.valor));
                }
                else if (partes.length == 2) {
                    try {
                        Join<Object, Object> join = root.join(partes[0]);
                        predicados.add(cb.equal(join.get(partes[1]), c.valor));
                    } catch (Exception e) {
                        predicados.add(cb.equal(root.get(partes[1]), c.valor));
                    }
                }
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}