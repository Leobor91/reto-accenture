package co.com.franquicia.model.product.gateway;

import co.com.franquicia.model.product.Product;
import co.com.franquicia.model.product.TopStockProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductGateway {

    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Mono<Product> findByName(String name);
    Mono<Product> updateStock(Long id, Integer newStock);
    Mono<Product> updateName(Long id, String newName);
    Mono<Void> deleteById(Long id);
    Flux<TopStockProduct> findTopStockByFranchise(Long franchiseId);

}
