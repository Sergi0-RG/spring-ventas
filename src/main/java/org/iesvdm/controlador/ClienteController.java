package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	// equivalente a @RequestMapping(value = "/clientes", method = RequestMethod.GET)
	@GetMapping("/clientes")
	public String listar(Model model) {

		List<Cliente> listaClientes = clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);

		// nombre de la plantilla: templates/clientes.html
		return "clientes";
	}

}
