package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.web.MessageUtil;
import io.gastos.gastos_app.web.user.UserEntryManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes
public class GastoController{

    private final GastoManager gastoManager;
    private Gasto gastoToEdit;
    private UserEntryManager userEntryManager;
    private final MessageUtil msg;

    public GastoController(GastoManager manager, UserEntryManager userEntryManager, MessageUtil msg) {
        this.gastoManager = manager;
        this.gastoToEdit = null;
        this.userEntryManager = userEntryManager;
        this.msg = msg;
    }


    @GetMapping("/gastosList")
    public String inicio(Model model) {
        model.addAttribute("editMode", false);
        fillAtributos(model, null);
        this.gastoToEdit = null;
        return "gastosList";
    }

    private void fillAtributos(Model model, Gasto gasto) {
        model.addAttribute("gasto", gasto != null ? gasto : new Gasto());
        model.addAttribute("gastos", gastoManager.findByUser(userEntryManager.getUsuario()));
    }

    @PostMapping("/gastos")
    public String guardar(@Valid @ModelAttribute("gasto") Gasto gasto,
                          BindingResult br,
                          Model model,
                          RedirectAttributes attrs) {
        if (!validate(gasto, attrs, br)) {
            fillAtributos(model, gasto);
            return "gastosList";
        }

        try{
            gastoManager.guardar(gasto);
            attrs.addFlashAttribute("success", msg.getMessage("gastoGuardado"));
        } catch (Exception e) {
            attrs.addFlashAttribute("error", msg.getMessage("gastoNoGuardado"));
        }
        return "redirect:/gastosList";                               // vuelve a GET /
    }

    private String goGastos(Model model, RedirectAttributes attrs) {
        model.addAttribute("gastos", gastoManager.findByUser(userEntryManager.getUsuario()));
        return "redirect:/gastosList";
    }


    @PostMapping("/gastos/borrar")
    public String borrar(@RequestParam("id") Long idGasto,
                         Model model,
                         RedirectAttributes attrs) {
        gastoManager.deleteGasto(idGasto);
        attrs.addFlashAttribute("success", msg.getMessage("gastoEliminado"));
        fillAtributos(model, null);
        return "redirect:/gastosList";

    }

    @PostMapping("/gastos/editar")
    public String editar(@RequestParam("id") Long idGasto,
                         Model model,
                         RedirectAttributes attrs) {
        Gasto attached = gastoManager.findGastoById(idGasto);

        if (attached == null) {
            attrs.addFlashAttribute("error", msg.getMessage("noExisteGasto", idGasto));
            fillAtributos(model, null);
            return "redirect:/gastosList";
        }
        model.addAttribute("gastos", gastoManager.findByUser(userEntryManager.getUsuario()));
        model.addAttribute("gasto", attached);
        model.addAttribute("editMode", true);
        this.gastoToEdit = attached;
        return "gastosList";
    }


    @PostMapping("/gastos/actualizar")
    public String actualizar(@Valid @ModelAttribute("gasto") Gasto gasto,
                             BindingResult br,
                             Model model,
                             RedirectAttributes attrs) {
        if (br.hasErrors()) {
            model.addAttribute("editMode", true);
            fillAtributos(model, gasto);
            attrs.addFlashAttribute("error", msg.getMessage("erroresForm"));
            return "gastosList";
        }
        if (!validate(gasto, attrs, br)) {
            return goGastos(model, attrs);
        }
        try{
            Gasto gastoActualizado = gastoManager.actualizarGasto(this.gastoToEdit, gasto);
            gastoManager.editGasto(gastoActualizado);
            attrs.addFlashAttribute("success", msg.getMessage("gastoActualizado"));
        }catch (NoPuedoActualizarGastoException ex){
            attrs.addFlashAttribute("error", msg.getMessage(ex.getMessage()));
        }
        return inicio(model);
    }

    private boolean validate(Gasto gasto,  RedirectAttributes attrs,  BindingResult br){
        if(gasto.getTipoGasto() == null ||
                (gasto.getTipoGasto() != null && gasto.getTipoGasto().isBlank())){
            attrs.addFlashAttribute("error", msg.getMessage("errorGuardarGasto"));
            return false;
        }
        if(br.hasErrors()){
            attrs.addFlashAttribute("error", msg.getMessage("erroresForm"));
            return false;
        }

        return true;
    }


}
