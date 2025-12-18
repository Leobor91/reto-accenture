package co.com.franquicia.jpa.adapter;

import co.com.franquicia.jpa.entity.BranchEntity;
import co.com.franquicia.jpa.mapper.BranchEntityMapper;
import co.com.franquicia.jpa.repository.BranchJpaRepository;
import co.com.franquicia.model.branch.Branch;
import co.com.franquicia.model.branch.gateway.BranchGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchGateway {

    private final BranchJpaRepository repository;
    private final BranchEntityMapper mapper;


    @Override
    @Transactional
    public Mono<Branch> save(Branch branch) {
        return Mono.fromCallable(() -> {
            BranchEntity saved = repository.save(mapper.toEntity(branch));
            return mapper.toModel(saved);
        });
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return Mono.fromCallable(() -> repository.findById(id)
                        .map(mapper::toModel)
                        .orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return Mono.fromCallable(() -> repository.findByFranchiseId(franchiseId))
                .flatMapMany(Flux::fromIterable)
                .map(mapper::toModel)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Branch> updateName(Long id, String newName) {
        return Mono.fromCallable(() -> {
            int updated = repository.updateName(id, newName);
            if (updated == 0) return null;
            return repository.findById(id).map(mapper::toModel).orElse(null);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Branch> findByName(String name) {
        return Mono.fromCallable(() -> repository.findByName(name)
                        .map(mapper::toModel)
                        .orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
