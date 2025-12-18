package co.com.franquicia.usecase.branch;

import co.com.franquicia.model.branch.Branch;
import co.com.franquicia.model.branch.gateway.BranchGateway;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final BranchGateway branchGateway;
    private final FranchiseGateway franchiseGateway;

    public Mono<Branch> execute(Long franchiseId, String name) {
        return franchiseGateway.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franquicia no encontrada con el id: " + franchiseId)
                ))
                .flatMap(franchise ->  branchGateway.findByName(name)
                            .flatMap(existing -> Mono.<Branch>error(
                                    new IllegalArgumentException("Ya existe una sucursal con el nombre: " + name)
                            ))
                            .switchIfEmpty(Mono.defer(() -> {
                                Branch branch = Branch.builder()
                                        .franchiseId(franchiseId)
                                        .name(name)
                                        .build();
                                return branchGateway.save(branch);
                            }))
                );
    }

}
