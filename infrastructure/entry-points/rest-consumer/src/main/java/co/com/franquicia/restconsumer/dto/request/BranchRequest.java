package co.com.franquicia.restconsumer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear una nueva sucursal")
public class BranchRequest {

    @Schema(
            description = "ID de la franquicia a la que pertenece",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 255
    )
    @JsonProperty("franquicia_id")
    private Long franchiseId;

    @Schema(
            description = "Nombre de la sucursal",
            example = "Sucursal Bogotá Norte",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 255
    )
    @JsonProperty("nombre_sucursal")
    private String name;

}
