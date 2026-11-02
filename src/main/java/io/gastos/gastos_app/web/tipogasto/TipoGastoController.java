package io.gastos.gastos_app.web.tipogasto;

import io.gastos.gastos_app.model.TipoGasto;
import io.gastos.gastos_app.web.MessageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tiposGasto")
public class TipoGastoController {

    private final TipoGastoManager tipoGastoManager;
    private final MessageUtil msg;

    public TipoGastoController(TipoGastoManager tipoGastoManager, MessageUtil msg) {
        this.tipoGastoManager = tipoGastoManager;
        this.msg = msg;
    }

    @GetMapping
    public String inicio(Model model) {
        model.addAttribute("tipoGasto", new TipoGasto());
        model.addAttribute("tiposGasto", tipoGastoManager.findDelUsuario());
        return "gasto/tiposGasto";
    }

    @PostMapping
    public String guardar(@ModelAttribute("tipoGasto") TipoGasto tipoGasto,
                          RedirectAttributes attrs) {
        boolean nombreVacio = tipoGasto.getNombre() == null || tipoGasto.getNombre().isBlank();

        if (nombreVacio) {
            attrs.addFlashAttribute("error", msg.getMessage("nombreTipoGastoObligatorio"));
        } else if (tipoGastoManager.existeNombre(tipoGasto.getNombre())) {
            attrs.addFlashAttribute("error", msg.getMessage("tipoGastoYaExiste"));
        } else {
            tipoGastoManager.guardar(tipoGasto);
            attrs.addFlashAttribute("success", msg.getMessage("tipoGastoGuardado"));
        }
        return "redirect:/tiposGasto";
    }

    @PostMapping("/borrar")
    public String borrar(@RequestParam("id") Long idTipoGasto,
                         RedirectAttributes attrs) {
        if (tipoGastoManager.borrar(idTipoGasto)) {
            attrs.addFlashAttribute("success", msg.getMessage("tipoGastoEliminado"));
        } else {
            attrs.addFlashAttribute("error", msg.getMessage("noExisteTipoGasto", idTipoGasto));
        }
        return "redirect:/tiposGasto";
    }
}