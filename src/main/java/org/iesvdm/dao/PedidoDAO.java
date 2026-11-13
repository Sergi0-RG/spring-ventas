package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteTotalDTO;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;

public interface PedidoDAO {

	public void create(Pedido pedido);

	public List<Pedido> getAll();

	public Optional<Pedido> find(int id);

	public void update(Pedido pedido);

	public void delete(int id);

	// Consultas de lectura con JOIN (nombres de cliente y comercial)
	public List<PedidoDetalle> getAllDetalle();

	public Optional<PedidoDetalle> findDetalle(int id);

	// Pedidos de un comercial / de un cliente (con los nombres de cliente y comercial)
	public List<PedidoDetalle> getAllByComercial(int idComercial);

	public List<PedidoDetalle> getAllByCliente(int idCliente);

	// Clientes de un comercial con sus importes totales, de mayor a menor
	public List<ClienteTotalDTO> getClientesTotalByComercial(int idComercial);

}
