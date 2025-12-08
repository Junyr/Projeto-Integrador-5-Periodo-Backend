package com.obelix.pi.query;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsultaService {

    public boolean validarComando(String comando) {
        try {
            AnalisadorLexico lex = new AnalisadorLexico(comando);
            List<Token> tokens = lex.analisar();

            AnalisadorSintatico sintatico = new AnalisadorSintatico(tokens);
            sintatico.analisar();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}