package co.com.franquicia.restconsumer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @JsonProperty("sucursal_id")
    private Long branchId;

    @JsonProperty("nombre_producto")
    private String name;

    @JsonProperty("stock_producto")
    private Integer stock;

}
