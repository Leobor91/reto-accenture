package co.com.franquicia.usecase.product;

import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import co.com.franquicia.model.product.TopStockProduct;
import co.com.franquicia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetTopStockByFranchiseUseCase {

    private final ProductGateway productGateway;
    private final FranchiseGateway franchiseGateway;

    public Flux<TopStockProduct> execute(Long franchiseId) {
        return franchiseGateway.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franquicia no encontrada con el id: " + franchiseId)
                ))
                .flatMapMany(franchise -> productGateway.findTopStockByFranchise(franchiseId));
    }

}
