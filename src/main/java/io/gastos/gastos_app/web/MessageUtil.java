package io.gastos.gastos_app.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Clase utilidad para manejar los mensajes.
 * Para usarla hay que añadirla a la clase objetivo como:    private final MessageSource messageSource;
 * y rellenarla en el constructor como de costumbre.
 * Esta clase centraliza el uso de literales en la aplicacion, rellenando con el Locale asignado siempre.
 */
@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public MessageUtil(MessageSource ms) {
        this.messageSource = ms;
    }

    //Metodo que devuelve el literal "simple"
    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key,
                null, locale);
    }

    /**
     * Metodo que devuelve un literal 'complejo' con placeholders
     * String txtConNombre = msg.getMessage("gasto.creado.usuario", usuario.getUsername())
     */
    public String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key,
                args, locale);
    }
}
