package co.com.franquicia.usecase.branch;

import co.com.franquicia.model.branch.Branch;
import co.com.franquicia.model.branch.gateway.BranchGateway;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetBranchesByFranchiseUseCase {

    private final BranchGateway branchGateway;
    private final FranchiseGateway franchiseGateway;

    public Flux<Branch> execute(Long franchiseId) {
        return franchiseGateway.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franquicia no encontrada con este id: " + franchiseId)
                ))
                .flatMapMany(franchise -> branchGateway.findByFranchiseId(franchiseId));
    }

}
