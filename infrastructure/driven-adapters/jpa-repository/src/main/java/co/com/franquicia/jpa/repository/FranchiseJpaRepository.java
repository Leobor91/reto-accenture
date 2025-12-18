package co.com.franquicia.jpa.repository;

import co.com.franquicia.jpa.entity.FranchiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FranchiseJpaRepository extends JpaRepository<FranchiseEntity, Long> {

    Optional<FranchiseEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE FranchiseEntity f SET f.name = :name WHERE f.id = :id")
    int updateName(Long id, String name);

}
