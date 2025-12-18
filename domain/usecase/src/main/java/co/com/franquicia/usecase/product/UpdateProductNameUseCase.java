package co.com.franquicia.usecase.product;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.product.Product;
import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final ProductGateway gateway;

    public Mono<Product> execute(Long id, String newName) {
        return gateway.findById(id)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Producto no encontrado con el id: " + id)
                ))
                .flatMap(existing -> gateway.findByName(newName)
                        .flatMap(duplicate -> {
                            if (!duplicate.getId().equals(id)) {
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre del Producto '" + newName + "' ya está en uso")
                                );
                            }else if (duplicate.getId().equals(id)){
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre del Producto '" + newName + "' ya esta asignado a este Producto")
                                );
                            }
                            return Mono.just(existing);
                        })
                        .switchIfEmpty(Mono.just(existing))
                )
                .flatMap(franchise -> gateway.updateName(id, newName));
    }

}
