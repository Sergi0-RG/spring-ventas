package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ComercialService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComercialController {

	private final ComercialService comercialService;

	// Inyección por constructor: no hace falta @Autowired
	public ComercialController(ComercialService comercialService) {
		this.comercialService = comercialService;
	}

	// Los campos de texto vacíos del formulario se guardan como null (y no como "")
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@GetMapping("/comerciales")
	public String listar(Model model) {

		List<Comercial> listaComerciales = comercialService.listAll();
		model.addAttribute("listaComerciales", listaComerciales);

		return "comerciales";
	}

	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable Integer id) {

		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);

		return "detalle-comercial";
	}

	@GetMapping("/comerciales/crear")
	public String crear(Model model) {

		model.addAttribute("comercial", new Comercial());

		return "crear-comercial";
	}

	@PostMapping("/comerciales/crear")
	public String submitCrear(@ModelAttribute("comercial") Comercial comercial, RedirectAttributes ra) {

		comercialService.newComercial(comercial);
		ra.addFlashAttribute("mensaje", "Comercial creado correctamente.");

		return "redirect:/comerciales";
	}

	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {

		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);

		return "editar-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public String submitEditar(@PathVariable Integer id, @ModelAttribute("comercial") Comercial comercial,
			RedirectAttributes ra) {

		comercial.setId(id);
		comercialService.replaceComercial(comercial);
		ra.addFlashAttribute("mensaje", "Comercial actualizado correctamente.");

		return "redirect:/comerciales";
	}

	@PostMapping("/comerciales/borrar/{id}")
	public String submitBorrar(@PathVariable Integer id, RedirectAttributes ra) {

		try {
			comercialService.deleteComercial(id);
			ra.addFlashAttribute("mensaje", "Comercial eliminado correctamente.");
		} catch (DataIntegrityViolationException e) {
			// el comercial tiene pedidos asociados (clave foránea)
			ra.addFlashAttribute("error", "No se puede eliminar el comercial porque tiene pedidos asociados.");
		}

		return "redirect:/comerciales";
	}

}
