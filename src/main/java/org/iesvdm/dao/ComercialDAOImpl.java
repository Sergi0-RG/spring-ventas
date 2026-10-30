package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
//Un Repository es un componente y a su vez un estereotipo de Spring
//que forma parte de la 'capa de persistencia'.
@Repository
public class ComercialDAOImpl implements ComercialDAO {

	// Plantilla jdbc inyectada automáticamente por Spring
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** Inserta el nuevo Comercial, actualizando el id en el bean. */
	@Override
	public synchronized void create(Comercial comercial) {

		String sqlInsert = """
				INSERT INTO comercial (nombre, apellido1, apellido2, comisión)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, comercial.getNombre());
			ps.setString(idx++, comercial.getApellido1());
			ps.setString(idx++, comercial.getApellido2());
			ps.setFloat(idx, comercial.getComision());
			return ps;
		}, keyHolder);

		comercial.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	/** Devuelve lista con todos los Comerciales. */
	@Override
	public List<Comercial> getAll() {

		List<Comercial> lista = jdbcTemplate.query(
				"SELECT * FROM comercial",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")));

		log.info("Devueltos {} registros.", lista.size());

		return lista;
	}

	/** Devuelve Optional de Comercial con el ID dado. */
	@Override
	public Optional<Comercial> find(int id) {

		List<Comercial> lista = jdbcTemplate.query(
				"SELECT * FROM comercial WHERE id = ?",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")),
				id);

		if (lista.isEmpty()) {
			log.info("Comercial no encontrado.");
			return Optional.empty();
		}
		return Optional.of(lista.get(0));
	}

	/** Actualiza Comercial con los campos del bean según su ID. */
	@Override
	public void update(Comercial comercial) {

		int rows = jdbcTemplate.update("""
				UPDATE comercial SET
					nombre = ?,
					apellido1 = ?,
					apellido2 = ?,
					comisión = ?
				WHERE id = ?
				""",
				comercial.getNombre(),
				comercial.getApellido1(),
				comercial.getApellido2(),
				comercial.getComision(),
				comercial.getId());

		log.info("Update de Comercial con {} registros actualizados.", rows);
	}

	/** Borra Comercial con el ID proporcionado. */
	@Override
	public void delete(int id) {

		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);

		log.info("Delete de Comercial con {} registros eliminados.", rows);
	}

}
