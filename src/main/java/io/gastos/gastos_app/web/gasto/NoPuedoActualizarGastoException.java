package io.gastos.gastos_app.web.gasto;

public class NoPuedoActualizarGastoException extends RuntimeException {

    public NoPuedoActualizarGastoException(String keyMessage) {
        super(keyMessage);
    }

}
