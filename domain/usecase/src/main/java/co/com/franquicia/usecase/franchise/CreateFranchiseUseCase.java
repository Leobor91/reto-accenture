package co.com.franquicia.usecase.franchise;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseGateway gateway;

    public Mono<Franchise> execute(String name) {
        return gateway.findByName(name)
                .flatMap(existing -> Mono.<Franchise>error(
                        new IllegalArgumentException("La franquicia con nombre '" + name + "' ya existe.")
                ))
                .switchIfEmpty(Mono.defer(() -> {
                    Franchise franchise = Franchise.builder()
                            .name(name)
                            .build();
                    return gateway.save(franchise);
                }));
    }
}
