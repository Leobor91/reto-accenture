package co.com.franquicia.jpa.adapter;

import co.com.franquicia.jpa.mapper.ProductEntityMapper;
import co.com.franquicia.jpa.repository.ProductJpaRepository;
import co.com.franquicia.model.product.Product;
import co.com.franquicia.model.product.TopStockProduct;
import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductGateway {

    private final ProductJpaRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    @Transactional
    public Mono<Product> save(Product product) {
        return Mono.fromCallable(() -> {
            product.validateStock();
            return mapper.toModel(repository.save(mapper.toEntity(product)));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Product> findById(Long id) {
        return Mono.fromCallable(() -> repository.findById(id)
                        .map(mapper::toModel)
                        .orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return Mono.fromCallable(() -> repository.findByBranchId(branchId))
                .flatMapMany(Flux::fromIterable)
                .map(mapper::toModel)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Product> updateStock(Long id, Integer newStock) {
        return Mono.fromCallable(() -> {
            if (newStock < 0) throw new IllegalArgumentException("Stock must be >= 0");
            int updated = repository.updateStock(id, newStock);
            if (updated == 0) return null;
            return repository.findById(id).map(mapper::toModel).orElse(null);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Product> updateName(Long id, String newName) {
        return Mono.fromCallable(() -> {
            int updated = repository.updateName(id, newName);
            if (updated == 0) return null;
            return repository.findById(id).map(mapper::toModel).orElse(null);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> repository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Flux<TopStockProduct> findTopStockByFranchise(Long franchiseId) {
        return Mono.fromCallable(() -> repository.findTopStockByFranchise(franchiseId))
                .flatMapMany(Flux::fromIterable)
                .map(row -> TopStockProduct.builder()
                        .branchId(row.getBranchId())
                        .branchName(row.getBranchName())
                        .productName(row.getProductName())
                        .stock(row.getStock())
                        .build())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
