# Ventas 0.0.4

CRUD de clientes, comerciales y pedidos con Spring Boot, JdbcTemplate y Thymeleaf (grid de Bootstrap).

1. Importar `src/main/resources/ventas.sql` en MySQL.
2. Ajustar usuario/contraseña en `application.properties`.
3. Ejecutar la aplicación desde el IDE (clase `VentasApplication`) o, con Maven
   instalado, desde la carpeta del proyecto con `mvn spring-boot:run`.
   El `CommandLineRunner` de `VentasApplication` lanza las pruebas de `ClienteDAO`,
   `ComercialDAO` y `PedidoDAO` y las muestra en el log.
4. Abrir en el navegador:
   - http://localhost:8080/clientes
   - http://localhost:8080/comerciales
   - http://localhost:8080/pedidos

Notas:
- Bootstrap se carga desde un CDN: hace falta conexión a internet para ver los estilos.
- Un cliente o comercial con pedidos no se puede borrar (clave foránea); la aplicación lo avisa.
