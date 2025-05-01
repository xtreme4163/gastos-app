package io.gastos.gastos_app.web.user;

import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.service.user.UserFacade;
import io.gastos.gastos_app.web.MessageUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('OWNER')")
public class UserController {

    private final UserFacade userFacade;
    private final PasswordEncoder encoder;
    private final MessageUtil msg;

    @Autowired
    public UserController(UserFacade userFacade, PasswordEncoder encoder, MessageUtil msg) {
        this.userFacade = userFacade;
        this.encoder = encoder;
        this.msg = msg;
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("userForm", new UserEntry());
        return "usuario-form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("userForm") UserEntry form,
                            BindingResult br,
                            RedirectAttributes attrs) {
        if (br.hasErrors()) {
            return "usuario-form";
        }

        if (userFacade.existsByUsername(form.getUsername())) {
            br.rejectValue("username", "duplicado", msg.getMessage("usuarioYaExiste"));
            return "usuario-form";
        }

        userFacade.create(form, encoder.encode(form.getPass()));
        attrs.addFlashAttribute("success", msg.getMessage("usuarioCreado"));
        return "redirect:/gastosList";
    }
}
