package co.com.franquicia.restconsumer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear una nueva franquicia")
public class FranchiseRequest {

    @Schema(
            description = "Nombre único de la franquicia",
            example = "Franquicia Colombia",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 255
    )
    @JsonProperty("nombre_franquicia")
    private String name;

}
