package org.iesvdm.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClienteController {

	private final ClienteService clienteService;
	private final ComercialService comercialService;

	// Inyección por constructor: no hace falta @Autowired
	public ClienteController(ClienteService clienteService, ComercialService comercialService) {
		this.clienteService = clienteService;
		this.comercialService = comercialService;
	}

	// Los campos de texto vacíos del formulario se guardan como null (y no como "")
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@GetMapping("/")
	public String inicio() {
		return "redirect:/clientes";
	}

	// /clientes  ->  todos
	// /clientes?ciudad=Sevilla  ->  solo los de esa ciudad
	@GetMapping("/clientes")
	public String listar(Model model, @RequestParam(required = false) String ciudad) {

		return listarPorCiudad(model, ciudad);
	}

	// /clientes/ciudad/Sevilla  ->  equivalente a /clientes?ciudad=Sevilla
	@GetMapping("/clientes/ciudad/{ciudad}")
	public String listarPorRuta(Model model, @PathVariable String ciudad) {

		return listarPorCiudad(model, ciudad);
	}

	private String listarPorCiudad(Model model, String ciudad) {

		List<Cliente> listaClientes = clienteService.listByCiudad(ciudad);
		model.addAttribute("listaClientes", listaClientes);
		model.addAttribute("listaCiudades", clienteService.listCiudades());
		model.addAttribute("ciudadSeleccionada", ciudad == null ? "" : ciudad.trim());

		return "clientes";
	}

	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id) {

		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("listaPedidos", clienteService.listPedidos(id));

		return "detalle-cliente";
	}

	@GetMapping("/clientes/crear")
	public String crear(Model model) {

		model.addAttribute("cliente", new Cliente());

		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public String submitCrear(@ModelAttribute("cliente") Cliente cliente, RedirectAttributes ra) {

		clienteService.newCliente(cliente);
		ra.addFlashAttribute("mensaje", "Cliente creado correctamente.");

		return "redirect:/clientes";
	}

	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {

		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		model.addAttribute("cliente", cliente);

		// formulario aparte para añadir comerciales: fecha de hoy y prioridad 1 por defecto
		model.addAttribute("vinculo", new ClienteComercial(id, null, LocalDate.now(), 1));
		cargarComerciales(model, id);

		return "editar-cliente";
	}

	@PostMapping("/clientes/editar/{id}")
	public String submitEditar(@PathVariable Integer id, @ModelAttribute("cliente") Cliente cliente,
			RedirectAttributes ra) {

		cliente.setId(id);
		clienteService.replaceCliente(cliente);
		ra.addFlashAttribute("mensaje", "Cliente actualizado correctamente.");

		return "redirect:/clientes";
	}

	@PostMapping("/clientes/borrar/{id}")
	public String submitBorrar(@PathVariable Integer id, RedirectAttributes ra) {

		try {
			clienteService.deleteCliente(id);
			ra.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
		} catch (DataIntegrityViolationException e) {
			// el cliente tiene pedidos o comerciales asociados (clave foránea)
			ra.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene pedidos o comerciales asociados.");
		}

		return "redirect:/clientes";
	}

	// Añade un comercial al cliente (formulario aparte del de edición) y vuelve a editar
	@PostMapping("/clientes/editar/{id}/comerciales/anadir")
	public String submitAnadirComercial(@PathVariable Integer id,
			@ModelAttribute("vinculo") ClienteComercial vinculo, BindingResult result,
			Model model, RedirectAttributes ra) {

		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		vinculo.setClienteId(id);

		List<String> errores = new ArrayList<>();
		if (result.hasErrors()) {
			errores.add("Revisa los datos: la fecha o la prioridad no tienen un formato válido.");
		}
		if (vinculo.getComercialId() == null || comercialService.one(vinculo.getComercialId()) == null) {
			errores.add("El comercial seleccionado no existe.");
		} else if (clienteService.existsVinculo(id, vinculo.getComercialId())) {
			errores.add("Ese comercial ya está vinculado al cliente.");
		}
		if (vinculo.getFecha() == null) {
			errores.add("La fecha es obligatoria.");
		}
		if (vinculo.getPrioridad() == null || vinculo.getPrioridad() < 1) {
			errores.add("La prioridad es obligatoria y debe ser un número mayor o igual que 1.");
		}

		if (!errores.isEmpty()) {
			// se vuelve al formulario de editar mostrando los errores
			model.addAttribute("cliente", cliente);
			model.addAttribute("errores", errores);
			cargarComerciales(model, id);
			return "editar-cliente";
		}

		clienteService.addComercial(vinculo);
		ra.addFlashAttribute("mensaje", "Comercial añadido al cliente.");

		return "redirect:/clientes/editar/" + id;
	}

	// Borra el vínculo cliente-comercial y vuelve a editar
	@PostMapping("/clientes/editar/{id}/comerciales/borrar/{comercialId}")
	public String submitBorrarComercial(@PathVariable Integer id, @PathVariable Integer comercialId,
			RedirectAttributes ra) {

		clienteService.removeComercial(id, comercialId);
		ra.addFlashAttribute("mensaje", "Comercial desvinculado del cliente.");

		return "redirect:/clientes/editar/" + id;
	}

	/** Carga el listado de comerciales vinculados y los disponibles para el select. */
	private void cargarComerciales(Model model, int clienteId) {
		model.addAttribute("listaComercialesVinculados", clienteService.listComercialesVinculados(clienteId));
		model.addAttribute("listaComercialesDisponibles", clienteService.listComercialesDisponibles(clienteId));
	}

}
