package io.gastos.gastos_app.web.user;

import io.gastos.gastos_app.model.user.UserEntry;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserEntryManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserEntry usuario;
    

    public UserEntry getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntry usuario) {
        this.usuario = usuario;
    }


}
