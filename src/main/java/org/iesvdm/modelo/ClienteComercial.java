package org.iesvdm.modelo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Vínculo N:M entre un cliente y un comercial (tabla cliente_has_comercial). */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteComercial {

	private Integer clienteId;
	private Integer comercialId;

	// formato ISO (yyyy-MM-dd) que envía y espera <input type="date">
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fecha;

	private Integer prioridad;

}
