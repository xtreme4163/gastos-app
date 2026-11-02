package io.gastos.gastos_app.web.user;

import io.gastos.gastos_app.model.user.TipoUsuario;
import io.gastos.gastos_app.model.util.consultauser.FiltroConsultaUsuario;
import io.gastos.gastos_app.service.user.UserFacade;
import io.gastos.gastos_app.web.MessageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

/**
 * Consulta de usuarios de la aplicación con filtro por nombre y tipo.
 */
@Controller
@RequestMapping("/usuarios/consulta")
@PreAuthorize("hasRole('OWNER')")
public class UserConsultaController {

    private final UserFacade userFacade;
    private final UserEntryManager userEntryManager;
    private final MessageUtil msg;

    public UserConsultaController(UserFacade userFacade, UserEntryManager userEntryManager, MessageUtil msg) {
        this.userFacade = userFacade;
        this.userEntryManager = userEntryManager;
        this.msg = msg;
    }

    @GetMapping
    public String consultar(@ModelAttribute("filtro") FiltroConsultaUsuario filtro,
                            Model model) {
        model.addAttribute("usuarios", userFacade.findByFiltro(filtro));
        model.addAttribute("tipos", TipoUsuario.values());
        return "userlist/usuarios-list";
    }

    @PostMapping("/borrar")
    public String borrar(@RequestParam("id") Long idUsuario,
                         RedirectAttributes attrs) {
        boolean esUsuarioLogado = Objects.equals(idUsuario, userEntryManager.getUsuario().getId());

        if (esUsuarioLogado) {
            attrs.addFlashAttribute("error", msg.getMessage("noPuedesBorrarteATiMismo"));
        } else if (userFacade.deleteConGastos(idUsuario)) {
            attrs.addFlashAttribute("success", msg.getMessage("usuarioEliminado"));
        } else {
            attrs.addFlashAttribute("error", msg.getMessage("noExisteUsuario", idUsuario));
        }
        return "redirect:/usuarios/consulta";
    }
}