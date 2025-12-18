package co.com.franquicia.usecase.product;

import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductGateway gateway;

    public Mono<Void> execute(Long productId) {
        return gateway.findById(productId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Producto no encontrado con el id: " + productId)
                ))
                .flatMap(product -> gateway.deleteById(productId));
    }

}
