package co.com.franquicia.usecase.branch;

import co.com.franquicia.model.branch.Branch;
import co.com.franquicia.model.branch.gateway.BranchGateway;
import co.com.franquicia.model.franchise.Franchise;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchGateway gateway;

    public Mono<Branch> execute(Long id, String newName) {
        return gateway.findById(id)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Sucursal no encontrada con el id: " + id)
                )).flatMap(existing -> gateway.findByName(newName)
                        .flatMap(duplicate -> {
                            if (!duplicate.getId().equals(id)) {
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre de la Sucursal '" + newName + "' ya está en uso")
                                );
                            }else if (duplicate.getId().equals(id)){
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre de la Sucursal '" + newName + "' ya esta asignado a esta Sucursal")
                                );
                            }
                            return Mono.just(existing);
                        })
                        .switchIfEmpty(Mono.just(existing))
                )
                .flatMap(franchise -> gateway.updateName(id, newName));
    }

}
