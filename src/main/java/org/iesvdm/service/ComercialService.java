package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;
	private final PedidoDAO pedidoDAO;

	// Inyección por constructor: no hace falta @Autowired
	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	/** Devuelve el comercial o null si no existe. */
	public Comercial one(int id) {
		Optional<Comercial> opt = comercialDAO.find(id);
		return opt.orElse(null);
	}

	public void newComercial(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}

	/** Pedidos realizados por el comercial, con el nombre del cliente asociado. */
	public List<PedidoDetalle> listPedidos(int id) {
		return pedidoDAO.getAllByComercial(id);
	}

}
