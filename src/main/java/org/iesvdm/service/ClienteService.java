package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

	private final ClienteDAO clienteDAO;
	private final PedidoDAO pedidoDAO;

	// Inyección por constructor: no hace falta @Autowired
	public ClienteService(ClienteDAO clienteDAO, PedidoDAO pedidoDAO) {
		this.clienteDAO = clienteDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Cliente> listAll() {
		return clienteDAO.getAll();
	}

	/** Devuelve el cliente o null si no existe. */
	public Cliente one(int id) {
		Optional<Cliente> opt = clienteDAO.find(id);
		return opt.orElse(null);
	}

	public void newCliente(Cliente cliente) {
		clienteDAO.create(cliente);
	}

	public void replaceCliente(Cliente cliente) {
		clienteDAO.update(cliente);
	}

	public void deleteCliente(int id) {
		clienteDAO.delete(id);
	}

	/** Pedidos realizados por el cliente, con el nombre del comercial asociado. */
	public List<PedidoDetalle> listPedidos(int id) {
		return pedidoDAO.getAllByCliente(id);
	}

	/** Clientes de la ciudad dada. Si la ciudad es nula o vacía, devuelve todos. */
	public List<Cliente> listByCiudad(String ciudad) {
		if (ciudad == null || ciudad.isBlank()) {
			return clienteDAO.getAll();
		}
		return clienteDAO.getAllByCiudad(ciudad.trim());
	}

	/** Ciudades distintas, para el desplegable del filtro. */
	public List<String> listCiudades() {
		return clienteDAO.getCiudades();
	}

}
