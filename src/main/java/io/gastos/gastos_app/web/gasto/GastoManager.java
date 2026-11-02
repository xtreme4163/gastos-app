package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.gasto.Gasto;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.gasto.GastoFacade;
import io.gastos.gastos_app.web.tipogasto.TipoGastoManager;
import io.gastos.gastos_app.web.user.UserEntryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GastoManager {

    private final GastoFacade gastoFacade;
    private final UserEntryManager userManager;
    private final TipoGastoManager tipoGastoManager;

    @Autowired
    public GastoManager(GastoFacade gastoFacade, UserEntryManager userManager, TipoGastoManager tipoGastoManager) {
        this.gastoFacade = gastoFacade;
        this.userManager = userManager;
        this.tipoGastoManager = tipoGastoManager;
    }

    public Gasto guardar(Gasto gasto) {
        validarTipoGasto(gasto);
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
        validarTipoGasto(nuevo);

        original.setConcepto(nuevo.getConcepto());
        original.setImporte(nuevo.getImporte());
        original.setFechaGasto(nuevo.getFechaGasto());
        return original;
    }

    /** Evita asociar un tipo de gasto de otro usuario (id manipulado en el formulario). */
    private void validarTipoGasto(Gasto gasto) {
        boolean tipoValido = tipoGastoManager.esValidoParaUsuario(gasto.getTipoGasto());
        if (!tipoValido) {
            throw new NoPuedoActualizarGastoException("tipoGastoNoAceptado");
        }
    }

}