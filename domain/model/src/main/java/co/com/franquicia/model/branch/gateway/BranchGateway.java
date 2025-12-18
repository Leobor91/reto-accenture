package co.com.franquicia.model.branch.gateway;

import co.com.franquicia.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchGateway {

    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(Long id);
    Flux<Branch> findByFranchiseId(Long franchiseId);
    Mono<Branch> updateName(Long id, String newName);
    Mono<Branch> findByName(String name);

}
