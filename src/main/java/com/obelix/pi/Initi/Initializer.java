package com.obelix.pi.Initi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.ResiduoRepo;

@Component
// Padrão Template Method: fluxo de inicialização
public class Initializer implements CommandLineRunner {

    @Autowired
    private ResiduoRepo residuoRepo;

    @Override
    public void run(String... args) throws Exception {
        inicializarResiduos();
        // outros métodos CSV...
    }

    // Padrão Factory Method: criação de resíduos
    private Residuo criarResiduo(String tipo) {
        Residuo residuo = new Residuo();
        residuo.setTipo(tipo);
        return residuo;
    }

    private void inicializarResiduos() {
        String[] tipos = {"Metal","Plástico","Papel","Orgânico"};
        for(String tipo : tipos) {
            if(!residuoRepo.existsByTipo(tipo)) {
                residuoRepo.save(criarResiduo(tipo));
            }
        }
    }
}
