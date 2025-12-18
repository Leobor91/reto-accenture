package co.com.franquicia.usecase.franchise;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetAllFranchisesUseCase {

    private final FranchiseGateway gateway;

    public Flux<Franchise> execute() {
        return gateway.findAll();
    }

}
