package co.com.franquicia.usecase.product;

import co.com.franquicia.model.product.Product;
import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductGateway gateway;

    public Mono<Product> execute(Long productId, Integer newStock) {
        return gateway.findById(productId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Producto no encontrado con id: " + productId)
                ))
                .flatMap(product -> gateway.updateStock(productId, newStock));
    }

}
