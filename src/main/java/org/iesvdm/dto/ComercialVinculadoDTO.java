package org.iesvdm.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Comercial vinculado a un cliente, con el nombre completo, la fecha y la prioridad del vínculo. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComercialVinculadoDTO {

	private int comercialId;
	private String comercialNombre;
	private LocalDate fecha;
	private int prioridad;

}
