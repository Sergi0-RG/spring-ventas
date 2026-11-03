package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
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
public class ClienteController {

	private final ClienteService clienteService;

	// Inyección por constructor: no hace falta @Autowired
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
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

	@GetMapping("/clientes")
	public String listar(Model model) {

		List<Cliente> listaClientes = clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);

		return "clientes";
	}

	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id) {

		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		model.addAttribute("cliente", cliente);

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
			// el cliente tiene pedidos asociados (clave foránea)
			ra.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene pedidos asociados.");
		}

		return "redirect:/clientes";
	}

}
