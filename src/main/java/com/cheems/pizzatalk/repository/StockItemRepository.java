package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.StockItemEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, Long>, JpaSpecificationExecutor<StockItemEntity> {
    @Modifying
    @Query("DELETE FROM StockItemEntity stockItemEntity WHERE stockItemEntity.store.id = ?1")
    void deleteByStoreId(Long storeId);

    @Modifying
    @Query("DELETE FROM StockItemEntity stockItemEntity WHERE stockItemEntity.product.id = ?1")
    void deleteByProductId(Long productId);

    @Modifying
    @Query("DELETE FROM StockItemEntity stockItemEntity WHERE stockItemEntity.optionDetail.id = ?1")
    void deleteByOptionDetailId(Long optionDetailId);

    @Query(
        "SELECT stockItemEntity FROM StockItemEntity stockItemEntity" +
        " WHERE stockItemEntity.store.id = ?1" +
        " AND stockItemEntity.product.id IN ?2"
    )
    Set<StockItemEntity> findByStoreIdAndProductIds(Long storeId, Set<Long> productIds);

    @Query(
        "SELECT stockItemEntity FROM StockItemEntity stockItemEntity" +
        " WHERE stockItemEntity.product.id = ?1" +
        " AND stockItemEntity.store.id IN ?2"
    )
    Set<StockItemEntity> findByProductIdAndStoreIds(Long productId, Set<Long> storeIds);

    @Query(
        "SELECT stockItemEntity FROM StockItemEntity stockItemEntity" +
        " WHERE stockItemEntity.store.id = ?1" +
        " AND stockItemEntity.optionDetail.id IN ?2"
    )
    Set<StockItemEntity> findByStoreIdAndOptionDetailIds(Long store, Set<Long> optionDetailIds);

    @Query(
        "SELECT stockItemEntity FROM StockItemEntity stockItemEntity" +
        " WHERE stockItemEntity.optionDetail.id = ?1" +
        " AND stockItemEntity.store.id IN ?2"
    )
    Set<StockItemEntity> findByOptionDetailIdAndStoreIds(Long optionDetailId, Set<Long> stores);

    @SuppressWarnings("NullableProblems")
    List<StockItemEntity> findAll(@Nullable Specification<StockItemEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<StockItemEntity> findAll(@Nullable Specification<StockItemEntity> spec, Pageable pageable);
}
