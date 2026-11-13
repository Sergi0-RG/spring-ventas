package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO con un cliente de un comercial y sus importes totales de pedidos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteTotalDTO {

	private int clienteId;
	private String clienteNombre;
	// suma de los pedidos del cliente con el comercial consultado
	private double totalConComercial;
	// suma de todos los pedidos del cliente (con cualquier comercial)
	private double totalCliente;

}
