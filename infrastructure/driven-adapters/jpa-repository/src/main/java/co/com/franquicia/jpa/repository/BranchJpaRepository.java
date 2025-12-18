package co.com.franquicia.jpa.repository;

import co.com.franquicia.jpa.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BranchJpaRepository extends JpaRepository<BranchEntity, Long> {

    List<BranchEntity> findByFranchiseId(Long franchiseId);

    Optional<BranchEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE BranchEntity b SET b.name = :name WHERE b.id = :id")
    int updateName(@Param("id") Long id, @Param("name") String name);

}
