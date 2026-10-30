package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Comercial;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;

	// Inyección por constructor: no hace falta @Autowired
	public ComercialService(ComercialDAO comercialDAO) {
		this.comercialDAO = comercialDAO;
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public Optional<Comercial> one(int id) {
		return comercialDAO.find(id);
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

}
