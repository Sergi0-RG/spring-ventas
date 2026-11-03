package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

	private final ClienteDAO clienteDAO;

	// Inyección por constructor: no hace falta @Autowired
	public ClienteService(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
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

}
