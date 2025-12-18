package co.com.franquicia.model.franchise;

import co.com.franquicia.model.branch.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {

    private Long id;
    private String name;

}