package io.gastos.gastos_app.web.user;

import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserManager {

    private UserEntry usuario;
    private final UserFacade userFacade;

    @Autowired
    public UserManager(UserFacade facade){
        this.userFacade = facade;
    }

    public UserEntry getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntry usuario) {
        this.usuario = usuario;
    }

    public UserEntry findByPassAndUsername(String pass, String username){
        return userFacade.findByPassAndUsername(pass, username);
    }


}
