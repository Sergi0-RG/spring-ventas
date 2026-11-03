# Ventas 0.0.3

CRUD de clientes y comerciales con Spring Boot, JdbcTemplate y Thymeleaf (grid de Bootstrap).

1. Importar `src/main/resources/ventas.mysql` en MySQL.
2. Ajustar usuario/contraseña en `application.properties`.
3. Ejecutar la aplicación desde el IDE (clase `VentasApplication`) o, con Maven
   instalado, desde la carpeta del proyecto con `mvn spring-boot:run`.
   El `CommandLineRunner` de `VentasApplication` lanza las pruebas de `ClienteDAO`
   y `ComercialDAO` y las muestra en el log.
4. Abrir en el navegador:
   - http://localhost:8080/clientes
   - http://localhost:8080/comerciales

Nota: Bootstrap se carga desde un CDN, hace falta conexión a internet para ver los estilos.
