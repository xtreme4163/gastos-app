package io.gastos.gastos_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/img/**", "/js/**").permitAll()
                        .requestMatchers("/usuarios/**").hasRole("OWNER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")            // tu login.html
                        .defaultSuccessUrl("/gastosList", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(out -> out
                        .logoutUrl("/logout")           // botón salir
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    // codificador; BCrypt es el recomendado
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
