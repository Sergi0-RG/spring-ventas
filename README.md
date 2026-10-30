# Ventas 0.0.2

1. Importar `src/main/resources/ventas.mysql` en MySQL.
2. Ajustar usuario/contraseña en `application.properties`.
3. Ejecutar la aplicación desde el IDE (clase `VentasApplication`) o, con Maven
   instalado, desde la carpeta del proyecto con `mvn spring-boot:run`.
   El `CommandLineRunner` de `VentasApplication` lanza las pruebas de `ClienteDAO`
   y `ComercialDAO` y las muestra en el log.
4. Listados en el navegador: http://localhost:8080/clientes