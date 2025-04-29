package io.gastos.gastos_app.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@ControllerAdvice
public class GlobalBindingConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // creamos un NumberFormat para la locale española
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es","ES"));
        nf.setGroupingUsed(true);
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(2);

        // registramos CustomNumberEditor: convierte String→BigDecimal usando ese NumberFormat
        binder.registerCustomEditor(
                BigDecimal.class,
                new CustomNumberEditor(BigDecimal.class, nf, true)  // true = allowEmpty
        );
    }
}
