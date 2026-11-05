package org.iesvdm.modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de lectura para listados y detalle: datos del pedido junto con los
 * nombres completos del cliente y del comercial (resultado de un JOIN).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDetalle {

	private int id;
	private double total;
	private LocalDate fecha;
	private int clienteId;
	private int comercialId;
	private String clienteNombre;
	private String comercialNombre;

}
