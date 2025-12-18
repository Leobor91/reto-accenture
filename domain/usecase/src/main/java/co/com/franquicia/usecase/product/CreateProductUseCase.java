package co.com.franquicia.usecase.product;

import co.com.franquicia.model.branch.Branch;
import co.com.franquicia.model.branch.gateway.BranchGateway;
import co.com.franquicia.model.product.Product;
import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductGateway productGateway;
    private final BranchGateway branchGateway;

    public Mono<Product> execute(Long branchId, String name, Integer stock) {
        return branchGateway.findById(branchId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Sucursal no encontrada con el id: " + branchId)
                ))
                .flatMap(branch -> productGateway.findByName(name)
                        .flatMap(existing -> Mono.<Product>error(
                                new IllegalArgumentException("Ya existe un producto con el nombre: " + name)
                        ))
                        .switchIfEmpty(Mono.defer(() -> {
                            Product product = Product.builder()
                                    .branchId(branchId)
                                    .name(name)
                                    .stock(stock)
                                    .build();
                            return productGateway.save(product);
                        })));
    }

}
