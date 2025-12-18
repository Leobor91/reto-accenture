package co.com.franquicia.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private Long id;
    private Long branchId;
    private String name;
    private Integer stock;

    public void validateStock() {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
    }
}
