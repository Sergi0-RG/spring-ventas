package org.iesvdm.dao;

import java.util.List;

import org.iesvdm.dto.ComercialVinculadoDTO;
import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;

public interface ClienteComercialDAO {

	public void create(ClienteComercial vinculo);

	public void delete(int clienteId, int comercialId);

	public boolean exists(int clienteId, int comercialId);

	// Comerciales vinculados al cliente (con nombre, fecha y prioridad)
	public List<ComercialVinculadoDTO> getAllByCliente(int clienteId);

	// Comerciales todavía no vinculados al cliente (para el select)
	public List<Comercial> getDisponibles(int clienteId);

}
