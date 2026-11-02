package io.gastos.gastos_app.model.util.consultauser;

import io.gastos.gastos_app.model.user.TipoUsuario;

public class FiltroConsultaUsuario {

    private String username;
    private TipoUsuario tipoUsuario;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
