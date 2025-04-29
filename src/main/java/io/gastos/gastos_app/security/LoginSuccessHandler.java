package io.gastos.gastos_app.security;

import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.user.UserFacade;
import io.gastos.gastos_app.web.user.UserEntryManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserEntryManager userManager;
    private final UserFacade userFacade;   // para traer el UserEntry de DB

    public LoginSuccessHandler(UserEntryManager userManager,
                               UserFacade userFacade) {
        this.userManager = userManager;
        this.userFacade = userFacade;
        setDefaultTargetUrl("/gastosList");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String username = authentication.getName();               // “principal”
        UserEntry user = userFacade.findByUsername(username)
                .orElseThrow();               // debería existir

        userManager.setUsuario(user);                             // ⬅️  guardamos
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

