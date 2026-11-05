package org.iesvdm.service;

import java.util.List;

import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

	private final PedidoDAO pedidoDAO;

	// Inyección por constructor: no hace falta @Autowired
	public PedidoService(PedidoDAO pedidoDAO) {
		this.pedidoDAO = pedidoDAO;
	}

	/** Listado con los nombres de cliente y comercial (JOIN). */
	public List<PedidoDetalle> listAll() {
		return pedidoDAO.getAllDetalle();
	}

	/** Pedido con los ids, para el formulario de edición. Null si no existe. */
	public Pedido one(int id) {
		return pedidoDAO.find(id).orElse(null);
	}

	/** Pedido con los nombres de cliente y comercial. Null si no existe. */
	public PedidoDetalle oneDetalle(int id) {
		return pedidoDAO.findDetalle(id).orElse(null);
	}

	public void newPedido(Pedido pedido) {
		pedidoDAO.create(pedido);
	}

	public void replacePedido(Pedido pedido) {
		pedidoDAO.update(pedido);
	}

	public void deletePedido(int id) {
		pedidoDAO.delete(id);
	}

}
