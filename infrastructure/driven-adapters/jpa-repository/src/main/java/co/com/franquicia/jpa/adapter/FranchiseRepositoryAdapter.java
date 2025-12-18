package co.com.franquicia.jpa.adapter;

import co.com.franquicia.jpa.entity.FranchiseEntity;
import co.com.franquicia.jpa.mapper.FranchiseEntityMapper;
import co.com.franquicia.jpa.repository.FranchiseJpaRepository;
import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseGateway {

    private final FranchiseJpaRepository repository;
    private final FranchiseEntityMapper mapper;

    @Override
    @Transactional
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.fromCallable(() -> {
            FranchiseEntity saved = repository.save(mapper.toEntity(franchise));
            return mapper.toModel(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return Mono.fromCallable(() -> repository.findById(id)
                        .map(mapper::toModel)
                        .orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return Mono.fromCallable(() -> repository.findByName(name)
                        .map(mapper::toModel)
                        .orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Franchise> findAll() {
        return Mono.fromCallable(() -> repository.findAll())
                .flatMapMany(Flux::fromIterable)
                .map(mapper::toModel)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Franchise> updateName(Long id, String newName) {
        return Mono.fromCallable(() -> {
            int updated = repository.updateName(id, newName);
            if (updated == 0) return null;
            return repository.findById(id).map(mapper::toModel).orElse(null);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> repository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
