package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.gasto.GastoFacade;
import io.gastos.gastos_app.web.user.UserEntryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GastoManager {

    private final GastoFacade gastoFacade;
    private final UserEntryManager userManager;

    @Autowired
    public GastoManager(GastoFacade gastoFacade, UserEntryManager userManager) {
        this.gastoFacade = gastoFacade;
        this.userManager = userManager;
    }

    public Gasto guardar(Gasto gasto) {
        if (gasto.getFechaGasto() == null) {
            gasto.setFechaGasto(LocalDate.now());
        }
        gasto.setUsuario(userManager.getUsuario());
        return gastoFacade.create(gasto);
    }

    public List<Gasto> findAll() {
        return gastoFacade.findAll();
    }

    public List<Gasto> findByUser(UserEntry usuario) {
        return gastoFacade.findByUser(usuario);
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
            throw new NoPuedoActualizarGastoException("gastoSelecNoValido");
        }

        original.setConcepto(nuevo.getConcepto());
        original.setImporte(nuevo.getImporte());
        original.setFechaGasto(nuevo.getFechaGasto());
        return original;
    }


}