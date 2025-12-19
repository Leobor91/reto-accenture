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
@Schema(description = "Request para crear un nuevo producto")
public class ProductRequest {

    @Schema(
            description = "ID de la sucursal donde se agrega el producto",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("sucursal_id")
    private Long branchId;

    @Schema(
            description = "Nombre del producto",
            example = "Laptop Dell XPS 15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("nombre_producto")
    private String name;

    @Schema(
            description = "Cantidad inicial en inventario",
            example = "150",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0"
    )
    @JsonProperty("stock_producto")
    private Integer stock;

}
