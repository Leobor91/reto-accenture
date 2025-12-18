package co.com.franquicia.jpa.repository;

import co.com.franquicia.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByBranchId(Long branchId);

    Optional<ProductEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.stock = :stock WHERE p.id = :id")
    int updateStock(@Param("id") Long id, @Param("stock") Integer stock);

    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.name = :name WHERE p.id = :id")
    int updateName(@Param("id") Long id, @Param("name") String name);

    // Si necesitas una consulta nativa, usa nativeQuery = true y ajusta el resultado
    @Query(value = """
        SELECT DISTINCT ON (p.branch_id)
            p.branch_id, b.name as branch_name, p.name as product_name, p.stock
        FROM product p
        INNER JOIN branch b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
        ORDER BY p.branch_id, p.stock DESC
        """, nativeQuery = true)
    List<TopStockRow> findTopStockByFranchise(@Param("franchiseId") Long franchiseId);

    interface TopStockRow {
        Long getBranch_id();
        String getBranch_name();
        String getProduct_name();
        Integer getStock();
    }

}
