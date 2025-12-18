package co.com.franquicia.model.franchise.gateway;

import co.com.franquicia.model.franchise.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseGateway {

    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Mono<Franchise> findByName(String name);
    Flux<Franchise> findAll();
    Mono<Franchise> updateName(Long id, String newName);

}
