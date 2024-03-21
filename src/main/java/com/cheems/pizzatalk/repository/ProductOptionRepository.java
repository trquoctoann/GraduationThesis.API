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

    @Modifying
    @Query("DELETE FROM ProductOptionEntity productOptionEntity WHERE productOptionEntity.optionDetail.id = ?1")
    void deleteByOptionDetailId(Long optionDetailId);

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

    @Query(
        "SELECT productOptionEntity FROM ProductOptionEntity productOptionEntity" +
        " WHERE productOptionEntity.product.id = ?1" +
        " AND productOptionEntity.option.id = ?2" +
        " AND productOptionEntity.optionDetail.id IN ?3"
    )
    Set<ProductOptionEntity> findByProductIdAndOptionIdAndOptionDetailIds(Long productId, Long optionId, Set<Long> optionDetailIds);
}
