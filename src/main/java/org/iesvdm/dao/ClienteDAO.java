package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;

public interface ClienteDAO {

	public void create(Cliente cliente);

	public List<Cliente> getAll();

	public Optional<Cliente> find(int id);

	public void update(Cliente cliente);

	public void delete(int id);

	// Filtrado por ciudad
	public List<Cliente> getAllByCiudad(String ciudad);

	// Ciudades distintas (para el desplegable del filtro)
	public List<String> getCiudades();

}
