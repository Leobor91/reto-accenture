package co.com.franquicia.restconsumer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {

    @Schema(description = "Respuesta HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "El stock no puede ser negativo")
    private String message;

    @Schema(description = "Timestamp del error", example = "2025-12-18T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Path del endpoint que generó el error", example = "/api/v1/products/create")
    private String path;

}
