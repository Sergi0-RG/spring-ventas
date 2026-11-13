package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir a la vista las estadísticas de los pedidos de un comercial.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComercialEstadisticasDTO {

	private int numPedidos;
	private double total;
	private double media;
	private double maximo;
	private double minimo;

}
