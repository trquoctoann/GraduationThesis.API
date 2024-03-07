package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.ProductOptionEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long>, JpaSpecificationExecutor<ProductOptionEntity> {
    @Modifying
    @Query("DELETE FROM ProductOptionEntity productOptionEntity WHERE productOptionEntity.product.id = ?1")
    void deleteByProductId(Long productId);

    @Modifying
    @Query("DELETE FROM ProductOptionEntity productOptionEntity WHERE productOptionEntity.option.id = ?1")
    void deleteByOptionId(Long optionId);

    @Query(
        "SELECT productOptionEntity FROM ProductOptionEntity productOptionEntity" +
        " WHERE productOptionEntity.product.id = ?1" +
        " AND productOptionEntity.option.id IN ?2"
    )
    Set<ProductOptionEntity> findByProductIdAndOptionIds(Long productId, Set<Long> optionIds);

    @Query(
        "SELECT productOptionEntity FROM ProductOptionEntity productOptionEntity" +
        " WHERE productOptionEntity.option.id = ?1" +
        " AND productOptionEntity.product.id IN ?2"
    )
    Set<ProductOptionEntity> findByOptionIdAndProductIds(Long optionId, Set<Long> productIds);
}
