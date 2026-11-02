package io.gastos.gastos_app.web.tipogasto;

import io.gastos.gastos_app.model.TipoGasto;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.tipogasto.TipoGastoFacade;
import io.gastos.gastos_app.web.user.UserEntryManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TipoGastoManager {

    private final TipoGastoFacade tipoGastoFacade;
    private final UserEntryManager userManager;

    public TipoGastoManager(TipoGastoFacade tipoGastoFacade, UserEntryManager userManager) {
        this.tipoGastoFacade = tipoGastoFacade;
        this.userManager = userManager;
    }

    public List<TipoGasto> findDelUsuario() {
        return tipoGastoFacade.findByUser(userManager.getUsuario());
    }

    public boolean existeNombre(String nombre) {
        return tipoGastoFacade.existsByUserAndNombre(userManager.getUsuario(), nombre);
    }

    public TipoGasto guardar(TipoGasto tipoGasto) {
        tipoGasto.setNombre(tipoGasto.getNombre().trim());
        tipoGasto.setUsuario(userManager.getUsuario());
        return tipoGastoFacade.create(tipoGasto);
    }

    /** @return false si el tipo no existe o no pertenece al usuario logado */
    public boolean borrar(Long idTipoGasto) {
        TipoGasto attached = tipoGastoFacade.findById(idTipoGasto);
        if (!esDelUsuario(attached)) {
            return false;
        }
        tipoGastoFacade.delete(attached);
        return true;
    }

    /** Un tipo nulo (gasto sin tipo) se considera válido. */
    public boolean esValidoParaUsuario(TipoGasto tipoGasto) {
        return tipoGasto == null || esDelUsuario(tipoGasto);
    }

    private boolean esDelUsuario(TipoGasto tipoGasto) {
        UserEntry usuario = userManager.getUsuario();
        boolean existeTipo = tipoGasto != null && tipoGasto.getUsuario() != null;
        return existeTipo && Objects.equals(tipoGasto.getUsuario().getId(), usuario.getId());
    }
}