package co.com.franquicia.usecase.franchise;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseGateway gateway;

    public Mono<Franchise> execute(Long id, String newName) {
        return gateway.findById(id)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franquicia no encontrada con el id: " + id)
                ))
                .flatMap(existing -> gateway.findByName(newName)
                        .flatMap(duplicate -> {
                            if (!duplicate.getId().equals(id)) {
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre de la franquicia '" + newName + "' ya está en uso")
                                );
                            }else if (duplicate.getId().equals(id)){
                                return Mono.<Franchise>error(
                                        new IllegalArgumentException("El nombre de la franquicia '" + newName + "' ya esta asignado a esta franquicia")
                                );
                            }
                            return Mono.just(existing);
                        })
                        .switchIfEmpty(Mono.just(existing))
                )
                .flatMap(franchise -> gateway.updateName(id, newName));
    }

}
