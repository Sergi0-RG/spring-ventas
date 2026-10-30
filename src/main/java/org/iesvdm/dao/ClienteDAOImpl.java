package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClienteDAOImpl implements ClienteDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** Inserta el nuevo Cliente, actualizando el id en el bean. */
	@Override
	public synchronized void create(Cliente cliente) {

		String sqlInsert = """
				INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoría)
				VALUES (?, ?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, cliente.getNombre());
			ps.setString(idx++, cliente.getApellido1());
			ps.setString(idx++, cliente.getApellido2());
			ps.setString(idx++, cliente.getCiudad());
			ps.setInt(idx, cliente.getCategoria());
			return ps;
		}, keyHolder);

		cliente.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	@Override
	public List<Cliente> getAll() {

		List<Cliente> lista = jdbcTemplate.query(
				"SELECT * FROM cliente",
				(rs, rowNum) -> new Cliente(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getString("ciudad"),
						rs.getInt("categoría")));

		log.info("Devueltos {} registros.", lista.size());

		return lista;
	}

	@Override
	public Optional<Cliente> find(int id) {

		// query (en vez de queryForObject) para no lanzar excepción si no existe
		List<Cliente> lista = jdbcTemplate.query(
				"SELECT * FROM cliente WHERE id = ?",
				(rs, rowNum) -> new Cliente(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getString("ciudad"),
						rs.getInt("categoría")),
				id);

		if (lista.isEmpty()) {
			log.info("Cliente no encontrado.");
			return Optional.empty();
		}
		return Optional.of(lista.get(0));
	}

	@Override
	public void update(Cliente cliente) {

		int rows = jdbcTemplate.update("""
				UPDATE cliente SET
					nombre = ?,
					apellido1 = ?,
					apellido2 = ?,
					ciudad = ?,
					categoría = ?
				WHERE id = ?
				""",
				cliente.getNombre(),
				cliente.getApellido1(),
				cliente.getApellido2(),
				cliente.getCiudad(),
				cliente.getCategoria(),
				cliente.getId());

		log.info("Update de Cliente con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {

		int rows = jdbcTemplate.update("DELETE FROM cliente WHERE id = ?", id);

		log.info("Delete de Cliente con {} registros eliminados.", rows);
	}

}
