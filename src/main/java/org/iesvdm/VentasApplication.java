package org.iesvdm;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class VentasApplication implements CommandLineRunner {

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;

	public static void main(String[] args) {
		SpringApplication.run(VentasApplication.class, args);
	}

	// run SÓLO se ejecuta en el arranque de la aplicación
	@Override
	public void run(String... args) throws Exception {

		pruebasClienteDAO();
		pruebasComercialDAO();
	}

	private void pruebasClienteDAO() {

		log.info("********************************");
		log.info("*Prueba de arranque ClienteDAO*");
		log.info("********************************");

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);

		if (cliente.isPresent()) {
			log.info("Cliente {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();

			cliente.get().setNombre("Jose M");
			clienteDAO.update(cliente.get());
			log.info("Cliente {} tras update: {}", id, clienteDAO.find(id).orElse(null));

			// se restaura el valor original
			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());
			log.info("Cliente {} restaurado: {}", id, clienteDAO.find(id).orElse(null));
		}

		Cliente nuevo = new Cliente(0, "Prueba", "Test", "Nuevo", "Málaga", 150);
		clienteDAO.create(nuevo);
		log.info("Cliente creado con id {}: {}", nuevo.getId(), nuevo);
		clienteDAO.delete(nuevo.getId());
		log.info("Cliente {} tras delete: {}", nuevo.getId(), clienteDAO.find(nuevo.getId()));
	}

	private void pruebasComercialDAO() {

		log.info("**********************************");
		log.info("*Prueba de arranque ComercialDAO*");
		log.info("**********************************");

		// 1. getAll
		log.info("--- getAll ---");
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		// 2. find existente
		log.info("--- find (existente) ---");
		int id = 1;
		Optional<Comercial> comercial = comercialDAO.find(id);

		if (comercial.isPresent()) {
			log.info("Comercial {}: {}", id, comercial.get());

			// 3. update
			log.info("--- update ---");
			String nombreOld = comercial.get().getNombre();
			float comisionOld = comercial.get().getComision();

			comercial.get().setNombre("Daniel Modificado");
			comercial.get().setComision(0.20f);
			comercialDAO.update(comercial.get());
			log.info("Comercial {} tras update: {}", id, comercialDAO.find(id).orElse(null));

			// restauración de valores originales
			comercial.get().setNombre(nombreOld);
			comercial.get().setComision(comisionOld);
			comercialDAO.update(comercial.get());
			log.info("Comercial {} restaurado: {}", id, comercialDAO.find(id).orElse(null));
		}

		// 4. find inexistente
		log.info("--- find (inexistente) ---");
		log.info("Comercial 9999: {}", comercialDAO.find(9999));

		// 5. create (comprobando el id generado)
		log.info("--- create ---");
		Comercial nuevo = new Comercial(0, "Prueba", "Test", "Nuevo", 0.09f);
		comercialDAO.create(nuevo);
		log.info("Comercial creado con id {}: {}", nuevo.getId(), nuevo);
		log.info("Recuperado de BD: {}", comercialDAO.find(nuevo.getId()).orElse(null));

		// 6. delete
		log.info("--- delete ---");
		comercialDAO.delete(nuevo.getId());
		log.info("Comercial {} tras delete: {}", nuevo.getId(), comercialDAO.find(nuevo.getId()));

		log.info("Total comerciales tras pruebas: {}", comercialDAO.getAll().size());
	}

}
