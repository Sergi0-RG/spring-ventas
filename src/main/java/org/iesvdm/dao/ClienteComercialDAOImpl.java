package org.iesvdm.dao;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.dto.ComercialVinculadoDTO;
import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClienteComercialDAOImpl implements ClienteComercialDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void create(ClienteComercial vinculo) {

		int rows = jdbcTemplate.update("""
				INSERT INTO cliente_has_comercial (cliente_id, comercial_id, fecha, prioridad)
				VALUES (?, ?, ?, ?)
				""",
				vinculo.getClienteId(),
				vinculo.getComercialId(),
				vinculo.getFecha(),
				vinculo.getPrioridad());

		log.info("Insertados {} vínculos cliente-comercial.", rows);
	}

	@Override
	public void delete(int clienteId, int comercialId) {

		int rows = jdbcTemplate.update(
				"DELETE FROM cliente_has_comercial WHERE cliente_id = ? AND comercial_id = ?",
				clienteId, comercialId);

		log.info("Delete de vínculo cliente-comercial con {} registros eliminados.", rows);
	}

	@Override
	public boolean exists(int clienteId, int comercialId) {

		Integer n = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM cliente_has_comercial WHERE cliente_id = ? AND comercial_id = ?",
				Integer.class, clienteId, comercialId);

		return n != null && n > 0;
	}

	@Override
	public List<ComercialVinculadoDTO> getAllByCliente(int clienteId) {

		List<ComercialVinculadoDTO> lista = jdbcTemplate.query("""
				SELECT co.id,
					CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial,
					chc.fecha, chc.prioridad
				FROM cliente_has_comercial chc
				JOIN comercial co ON co.id = chc.comercial_id
				WHERE chc.cliente_id = ?
				ORDER BY chc.prioridad, co.id
				""",
				(rs, rowNum) -> new ComercialVinculadoDTO(rs.getInt("id"),
						rs.getString("comercial"),
						rs.getObject("fecha", LocalDate.class),
						rs.getInt("prioridad")),
				clienteId);

		log.info("Devueltos {} comerciales vinculados al cliente {}.", lista.size(), clienteId);

		return lista;
	}

	@Override
	public List<Comercial> getDisponibles(int clienteId) {

		return jdbcTemplate.query("""
				SELECT * FROM comercial
				WHERE id NOT IN (SELECT comercial_id FROM cliente_has_comercial WHERE cliente_id = ?)
				ORDER BY id
				""",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")),
				clienteId);
	}

}
