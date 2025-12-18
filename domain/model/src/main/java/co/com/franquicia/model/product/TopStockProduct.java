package co.com.franquicia.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TopStockProduct {

    private Long branchId;
    private String branchName;
    private String productName;
    private Integer stock;

}
