package io.gastos.gastos_app.security;

import io.gastos.gastos_app.model.user.UserEntry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetails implements UserDetails {

    private final UserEntry usuario;

    public UsuarioDetails(UserEntry usuario) { this.usuario = usuario; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
                "ROLE_" + usuario.getTipoUsuario().name()));
    }
    @Override public String getPassword() { return usuario.getPass(); }
    @Override public String getUsername() { return usuario.getUsername();  }

    // el resto a true
    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isAccountNonLocked()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()            { return true; }
}
