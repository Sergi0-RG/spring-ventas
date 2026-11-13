package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	// CONCAT_WS ignora los valores NULL, así que si apellido2 es nulo no queda
	// ni "null" ni un espacio de más.
	private static final String SQL_DETALLE = """
			SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
				CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente,
				CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial
			FROM pedido p
			JOIN cliente c ON p.id_cliente = c.id
			JOIN comercial co ON p.id_comercial = co.id
			""";

	// Mapeo común de las consultas con JOIN
	private static final RowMapper<PedidoDetalle> MAPPER_DETALLE = (rs, rowNum) -> new PedidoDetalle(
			rs.getInt("id"),
			rs.getDouble("total"),
			rs.getObject("fecha", LocalDate.class),
			rs.getInt("id_cliente"),
			rs.getInt("id_comercial"),
			rs.getString("cliente"),
			rs.getString("comercial"));

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** Inserta el nuevo Pedido, actualizando el id en el bean. */
	@Override
	public synchronized void create(Pedido pedido) {

		String sqlInsert = """
				INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setDouble(idx++, pedido.getTotal());
			if (pedido.getFecha() != null) {
				ps.setObject(idx++, pedido.getFecha());
			} else {
				ps.setNull(idx++, Types.DATE);
			}
			ps.setInt(idx++, pedido.getClienteId());
			ps.setInt(idx, pedido.getComercialId());
			return ps;
		}, keyHolder);

		pedido.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	@Override
	public List<Pedido> getAll() {

		List<Pedido> lista = jdbcTemplate.query(
				"SELECT * FROM pedido ORDER BY id",
				(rs, rowNum) -> new Pedido(rs.getInt("id"),
						rs.getDouble("total"),
						rs.getObject("fecha", LocalDate.class),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial")));

		log.info("Devueltos {} registros.", lista.size());

		return lista;
	}

	@Override
	public Optional<Pedido> find(int id) {

		List<Pedido> lista = jdbcTemplate.query(
				"SELECT * FROM pedido WHERE id = ?",
				(rs, rowNum) -> new Pedido(rs.getInt("id"),
						rs.getDouble("total"),
						rs.getObject("fecha", LocalDate.class),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial")),
				id);

		if (lista.isEmpty()) {
			log.info("Pedido no encontrado.");
			return Optional.empty();
		}
		return Optional.of(lista.get(0));
	}

	@Override
	public void update(Pedido pedido) {

		int rows = jdbcTemplate.update("""
				UPDATE pedido SET
					total = ?,
					fecha = ?,
					id_cliente = ?,
					id_comercial = ?
				WHERE id = ?
				""",
				pedido.getTotal(),
				pedido.getFecha(),
				pedido.getClienteId(),
				pedido.getComercialId(),
				pedido.getId());

		log.info("Update de Pedido con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {

		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);

		log.info("Delete de Pedido con {} registros eliminados.", rows);
	}

	@Override
	public List<PedidoDetalle> getAllDetalle() {

		List<PedidoDetalle> lista = jdbcTemplate.query(
				SQL_DETALLE + " ORDER BY p.id",
				MAPPER_DETALLE);

		log.info("Devueltos {} registros (detalle).", lista.size());

		return lista;
	}

	@Override
	public Optional<PedidoDetalle> findDetalle(int id) {

		List<PedidoDetalle> lista = jdbcTemplate.query(
				SQL_DETALLE + " WHERE p.id = ?",
				MAPPER_DETALLE,
				id);

		if (lista.isEmpty()) {
			log.info("Pedido no encontrado.");
			return Optional.empty();
		}
		return Optional.of(lista.get(0));
	}

	/** Pedidos realizados por el comercial dado, con el nombre del cliente. */
	@Override
	public List<PedidoDetalle> getAllByComercial(int idComercial) {

		List<PedidoDetalle> lista = jdbcTemplate.query(
				SQL_DETALLE + " WHERE p.id_comercial = ? ORDER BY p.id",
				MAPPER_DETALLE,
				idComercial);

		log.info("Devueltos {} pedidos del comercial {}.", lista.size(), idComercial);

		return lista;
	}

	/** Pedidos realizados por el cliente dado, con el nombre del comercial. */
	@Override
	public List<PedidoDetalle> getAllByCliente(int idCliente) {

		List<PedidoDetalle> lista = jdbcTemplate.query(
				SQL_DETALLE + " WHERE p.id_cliente = ? ORDER BY p.id",
				MAPPER_DETALLE,
				idCliente);

		log.info("Devueltos {} pedidos del cliente {}.", lista.size(), idCliente);

		return lista;
	}

}
