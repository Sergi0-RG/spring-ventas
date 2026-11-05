package org.iesvdm.modelo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pedido tal cual está en la tabla: las relaciones se representan por sus ids
 * (clienteId y comercialId), que son los que se envían y reciben en los formularios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

	private int id;
	private double total;

	// formato ISO (yyyy-MM-dd) que envía y espera <input type="date">
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fecha;

	// Integer (y no int) para poder detectar "no seleccionado" (null)
	private Integer clienteId;
	private Integer comercialId;

}
