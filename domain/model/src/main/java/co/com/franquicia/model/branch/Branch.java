package co.com.franquicia.model.branch;

import co.com.franquicia.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {

    private Long id;
    private Long franchiseId;
    private String name;

}
