package co.com.franquicia.restconsumer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Builder
@Schema(description = "Respuesta exitosa estándar con datos genéricos")
public class ApiResponseDto<T> {

    @Schema(description = "Código HTTP de la respuesta", example = "200")
    int status;

    @Schema(description = "Mensaje descriptivo", example = "Operación exitosa")
    String message;

    @Schema(description = "Datos de la respuesta")
    T data;

}
