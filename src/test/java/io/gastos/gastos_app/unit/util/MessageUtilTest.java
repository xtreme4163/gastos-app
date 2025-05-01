package io.gastos.gastos_app.unit.util;

import io.gastos.gastos_app.web.MessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MessageUtilTest {

    @Test
    @DisplayName("Devuelve el literal de messages.properties según locale")
    void devuelveLiteralCorrecto() {
        // 1) Preparamos un MessageSource simulado
        MessageSource ms = mock(MessageSource.class);
        when(ms.getMessage(eq("iniciarSesion"), any(), eq(Locale.ENGLISH)))
                .thenReturn("Sign in");

        // 2) Simulamos locale EN
        try (MockedStatic<LocaleContextHolder> ctx = mockStatic(LocaleContextHolder.class)) {
            ctx.when(LocaleContextHolder::getLocale).thenReturn(Locale.ENGLISH);

            // 3) Probamos nuestra utilidad
            MessageUtil util = new MessageUtil(ms);
            String texto = util.getMessage("iniciarSesion");

            assertEquals("Sign in", texto);
            verify(ms).getMessage("iniciarSesion", null, Locale.ENGLISH);
        }
    }
}
