package org.iesvdm.modelo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

	private int id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String ciudad;
	private int categoria;

	/** Nombre y apellidos separados por espacio, omitiendo los que sean nulos o vacíos. */
	public String getNombreCompleto() {
		return Stream.of(nombre, apellido1, apellido2)
				.filter(s -> s != null && !s.isBlank())
				.collect(Collectors.joining(" "));
	}

}
