// src/main/java/io/gastos/gastos_app/manager/GastoManager.java
package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.service.gasto.GastoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GastoManager {

    private final GastoFacade gastoFacade;

    @Autowired
    public GastoManager(GastoFacade gastoFacade) {
        this.gastoFacade = gastoFacade;
    }

    public Gasto guardar(Gasto gasto) {
        LocalDateTime fecha = LocalDateTime.now(); // Esto para guardar la fecha en la que se introdujo el gasto
        return gastoFacade.create(gasto);
    }

    public List<Gasto> findAll() {
        return gastoFacade.findAll();
    }

    public void deleteGasto(Long idGasto){
        Gasto attached = gastoFacade.findById(idGasto);
        gastoFacade.deleteGasto(attached);
    }

    public Gasto editGasto(Gasto gasto){
        return gastoFacade.editGasto(gasto);
    }

    public Gasto findGastoById(Long id){
        return gastoFacade.findById(id);
    }

    public Gasto actualizarGasto(Gasto original, Gasto nuevo){
        if(original == null || nuevo == null){
            throw new NoPuedoActualizarGastoException("❌ El gasto seleccionado no es válido.");
        }

        original.setTipoGasto(nuevo.getTipoGasto());
        original.setImporte(nuevo.getImporte());
        return original;
    }


}