package io.gastos.gastos_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.math.BigDecimal;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // patrón con agrupación de miles y siempre dos decimales:
        NumberStyleFormatter milesYDecimales = new NumberStyleFormatter("#,##0.00");
        // lo aplicamos a todos los BigDecimal:
        registry.addFormatterForFieldType(BigDecimal.class, milesYDecimales);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver r = new SessionLocaleResolver();
        r.setDefaultLocale(new Locale("es","ES"));
        return r;
    }
}
