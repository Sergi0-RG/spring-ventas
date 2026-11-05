package org.iesvdm.controlador;

import java.util.ArrayList;
import java.util.List;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.iesvdm.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PedidoController {

	private final PedidoService pedidoService;
	private final ClienteService clienteService;
	private final ComercialService comercialService;

	public PedidoController(PedidoService pedidoService, ClienteService clienteService,
			ComercialService comercialService) {
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
		this.comercialService = comercialService;
	}

	@GetMapping("/pedidos")
	public String listar(Model model) {

		List<PedidoDetalle> listaPedidos = pedidoService.listAll();
		model.addAttribute("listaPedidos", listaPedidos);

		return "pedidos";
	}

	@GetMapping("/pedidos/{id}")
	public String detalle(Model model, @PathVariable Integer id, RedirectAttributes ra) {

		PedidoDetalle pedido = pedidoService.oneDetalle(id);
		if (pedido == null) {
			ra.addFlashAttribute("error", "El pedido " + id + " no existe.");
			return "redirect:/pedidos";
		}
		model.addAttribute("pedido", pedido);

		return "detalle-pedido";
	}

	@GetMapping("/pedidos/crear")
	public String crear(Model model) {

		model.addAttribute("pedido", new Pedido());
		cargarSelects(model);

		return "crear-pedido";
	}

	@PostMapping("/pedidos/crear")
	public String submitCrear(@ModelAttribute("pedido") Pedido pedido, BindingResult result, Model model,
			RedirectAttributes ra) {

		List<String> errores = validar(pedido, result);
		if (!errores.isEmpty()) {
			model.addAttribute("errores", errores);
			cargarSelects(model);
			return "crear-pedido";
		}

		pedidoService.newPedido(pedido);
		ra.addFlashAttribute("mensaje", "Pedido creado correctamente.");

		return "redirect:/pedidos";
	}

	@GetMapping("/pedidos/editar/{id}")
	public String editar(Model model, @PathVariable Integer id, RedirectAttributes ra) {

		Pedido pedido = pedidoService.one(id);
		if (pedido == null) {
			ra.addFlashAttribute("error", "El pedido " + id + " no existe.");
			return "redirect:/pedidos";
		}
		model.addAttribute("pedido", pedido);
		cargarSelects(model);

		return "editar-pedido";
	}

	@PostMapping("/pedidos/editar/{id}")
	public String submitEditar(@PathVariable Integer id, @ModelAttribute("pedido") Pedido pedido,
			BindingResult result, Model model, RedirectAttributes ra) {

		if (pedidoService.one(id) == null) {
			ra.addFlashAttribute("error", "El pedido " + id + " no existe.");
			return "redirect:/pedidos";
		}
		pedido.setId(id);

		List<String> errores = validar(pedido, result);
		if (!errores.isEmpty()) {
			model.addAttribute("errores", errores);
			cargarSelects(model);
			return "editar-pedido";
		}

		pedidoService.replacePedido(pedido);
		ra.addFlashAttribute("mensaje", "Pedido actualizado correctamente.");

		return "redirect:/pedidos";
	}

	@PostMapping("/pedidos/borrar/{id}")
	public String submitBorrar(@PathVariable Integer id, RedirectAttributes ra) {

		pedidoService.deletePedido(id);
		ra.addFlashAttribute("mensaje", "Pedido eliminado correctamente.");

		return "redirect:/pedidos";
	}

	/** Carga las listas para los select de cliente y comercial. */
	private void cargarSelects(Model model) {
		model.addAttribute("listaClientes", clienteService.listAll());
		model.addAttribute("listaComerciales", comercialService.listAll());
	}

	/** Valida el formulario; devuelve la lista de errores (vacía si todo es correcto). */
	private List<String> validar(Pedido pedido, BindingResult result) {

		List<String> errores = new ArrayList<>();

		if (result.hasErrors()) {
			errores.add("Revisa los datos: el total o la fecha no tienen un formato válido.");
		}
		if (pedido.getTotal() < 0) {
			errores.add("El total no puede ser negativo.");
		}
		if (pedido.getClienteId() == null || clienteService.one(pedido.getClienteId()) == null) {
			errores.add("El cliente seleccionado no existe.");
		}
		if (pedido.getComercialId() == null || comercialService.one(pedido.getComercialId()) == null) {
			errores.add("El comercial seleccionado no existe.");
		}

		return errores;
	}

}
