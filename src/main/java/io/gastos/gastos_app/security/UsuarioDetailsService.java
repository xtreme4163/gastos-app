package io.gastos.gastos_app.security;

import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.user.UserFacade;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UserFacade userFacade;

    public UsuarioDetailsService(UserFacade userFacade){
        this.userFacade = userFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntry u = userFacade.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No existe: " + username));

        // Si en la BBDD guardas OWNER/USER SIN prefijo, añade tú el prefijo:
        String authority = "ROLE_" + u.getTipoUsuario();

        return User
                .withUsername(u.getUsername())
                .password(u.getPass())
                .authorities(authority)
                .build();
    }
}
