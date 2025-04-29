package io.gastos.gastos_app.web.gasto;

import io.gastos.gastos_app.model.Gasto;
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

    public GastoController(GastoManager manager, UserEntryManager userEntryManager) {
        this.gastoManager = manager;
        this.gastoToEdit = null;
        this.userEntryManager = userEntryManager;

    }


    @GetMapping("/gastosList")
    public String inicio(Model model) {
        model.addAttribute("editMode", false);
        fillAtributos(model);
        this.gastoToEdit = null;
        return "gastosList";
    }

    private void fillAtributos(Model model) {
        model.addAttribute("gasto", new Gasto());
        model.addAttribute("gastos", gastoManager.findByUser(userEntryManager.getUsuario()));
    }

    @PostMapping("/gastos")
    public String guardar(@Valid @ModelAttribute("gasto") Gasto gasto,
                          BindingResult br,
                          Model model,
                          RedirectAttributes attrs) {
        if (!validate(gasto, attrs, br)) {
            return goGastos(model, attrs);
        }

        try{
            gastoManager.guardar(gasto);
            attrs.addFlashAttribute("success", "✅ ¡Gasto guardado correctamente!");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", "❌ Error al guardar el gasto");
        }
        return "redirect:/gastosList";                               // vuelve a GET /
    }

    private String goGastos(Model model, RedirectAttributes attrs) {
        model.addAttribute("gastos", gastoManager.findByUser(userEntryManager.getUsuario()));
        return "gastosList";
    }


    @PostMapping("/gastos/borrar")
    public String borrar(@RequestParam("id") Long idGasto,
                         Model model,
                         RedirectAttributes attrs) {
        gastoManager.deleteGasto(idGasto);
        attrs.addFlashAttribute("success", "✅ Gasto eliminado correctamente");
        fillAtributos(model);
        return "redirect:/gastosList";

    }

    @PostMapping("/gastos/editar")
    public String editar(@RequestParam("id") Long idGasto,
                         Model model,
                         RedirectAttributes attrs) {
        Gasto attached = gastoManager.findGastoById(idGasto);

        if (attached == null) {
            attrs.addFlashAttribute("error", "No existe el gasto, id: " + idGasto);
            fillAtributos(model);
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
                         Model model,
                         BindingResult br,
                         RedirectAttributes attrs) {
        if (!validate(gasto, attrs, br)) {
            return goGastos(model, attrs);
        }
        try{
            Gasto gastoActualizado = gastoManager.actualizarGasto(this.gastoToEdit, gasto);
            gastoManager.editGasto(gastoActualizado);
            attrs.addFlashAttribute("success","✅ Gasto actualizado");
        }catch (NoPuedoActualizarGastoException ex){
            attrs.addFlashAttribute("error",ex.getMessage());
        }
        return inicio(model);
    }

    private boolean validate(Gasto gasto,  RedirectAttributes attrs,  BindingResult br){
        if(gasto.getTipoGasto() == null ||
                (gasto.getTipoGasto() != null && gasto.getTipoGasto().isBlank())){
            attrs.addFlashAttribute("error", "❌ Error al guardar el gasto, tipo de gasto no puede estar vacío.");
            return false;
        }
        if(br.hasErrors()){
            attrs.addFlashAttribute("error", "❌ Hay errores en el formulario");
            return false;
        }

        return true;
    }


}
