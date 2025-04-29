package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.Gasto;

public class NoPuedoActualizarGastoException extends RuntimeException {

    public NoPuedoActualizarGastoException(String message) {
        super(message);
    }

}
