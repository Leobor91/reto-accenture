package co.com.franquicia.restconsumer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Franquicias")
                        .version("1.0.0")
                        .description("""
                    Sistema reactivo para la administración centralizada de franquicias, 
                    sucursales e inventario de productos.
                    
                    **Arquitectura**: Clean Architecture + Spring WebFlux + PostgreSQL
                    
                    **Características**:
                    - Gestión de franquicias con nombres únicos
                    - Administración de sucursales por franquicia
                    - Control de inventario con validación de stock >= 0
                    - Reporte de productos con mayor stock por sucursal
                    """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@franquicia.com")
                                .url("https://github.com/tu-org/franquicias"))
                        .license(new License()
                                .name("Propietario")
                                .url("https://www.franquicia.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local"),
                        new Server()
                                .url("https://api.franquicia.com")
                                .description("Producción")
                ));
    }

}
