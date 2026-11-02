package io.gastos.gastos_app.web.tipogasto;

import io.gastos.gastos_app.model.TipoGasto;
import io.gastos.gastos_app.service.GastosCrudFacade;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Convierte el id que llega del <select> del formulario en un TipoGasto y viceversa.
 * La comprobación de que el tipo pertenece al usuario se hace en GastoManager.
 */
@Component
public class TipoGastoFormatter implements Formatter<TipoGasto> {

    private final GastosCrudFacade gastosCrudFacade;

    public TipoGastoFormatter(GastosCrudFacade gastosCrudFacade) {
        this.gastosCrudFacade = gastosCrudFacade;
    }

    @Override
    public TipoGasto parse(String text, Locale locale) {
        boolean sinTipo = text == null || text.isBlank();
        if (sinTipo) {
            return null;
        }
        return gastosCrudFacade.find(TipoGasto.class, Long.valueOf(text.trim())).orElse(null);
    }

    @Override
    public String print(TipoGasto tipoGasto, Locale locale) {
        boolean tieneId = tipoGasto != null && tipoGasto.getId() != null;
        return tieneId ? tipoGasto.getId().toString() : "";
    }
}