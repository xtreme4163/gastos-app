package io.gastos.gastos_app.config;

import io.gastos.gastos_app.web.tipogasto.TipoGastoFormatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.math.BigDecimal;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TipoGastoFormatter tipoGastoFormatter;

    public WebConfig(TipoGastoFormatter tipoGastoFormatter) {
        this.tipoGastoFormatter = tipoGastoFormatter;
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // patrón con agrupación de miles y siempre dos decimales:
        NumberStyleFormatter milesYDecimales = new NumberStyleFormatter("#,##0.00");
        // lo aplicamos a todos los BigDecimal:
        registry.addFormatterForFieldType(BigDecimal.class, milesYDecimales);
        // select de tipo de gasto: id <-> TipoGasto
        registry.addFormatter(tipoGastoFormatter);
    }

    private static final Locale ESPANOL = new Locale("es");
    private static final Locale INGLES = Locale.ENGLISH;

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver() {
            // Locale elegido por el usuario en el selector (se guarda en sesión)
            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
                boolean hayLocaleElegido = locale != null;
                super.setLocale(request, response, hayLocaleElegido ? normalizarLocale(locale) : null);
            }
        };
        // Sin nada en sesión: idioma del navegador (Accept-Language)
        resolver.setDefaultLocaleFunction(request -> normalizarLocale(request.getLocale()));
        return resolver;
    }

    /** Solo soportamos español e inglés: cualquier otro idioma pasa a inglés. */
    private static Locale normalizarLocale(Locale locale) {
        boolean esEspanol = locale != null && ESPANOL.getLanguage().equals(locale.getLanguage());
        return esEspanol ? ESPANOL : INGLES;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor cambioIdioma = new LocaleChangeInterceptor();
        cambioIdioma.setParamName("lang");
        cambioIdioma.setIgnoreInvalidLocale(true);
        registry.addInterceptor(cambioIdioma);
    }
}
