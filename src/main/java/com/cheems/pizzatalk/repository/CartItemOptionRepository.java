package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.CartItemOptionEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemOptionRepository
    extends JpaRepository<CartItemOptionEntity, Long>, JpaSpecificationExecutor<CartItemOptionEntity> {
    @Modifying
    @Query("DELETE FROM CartItemOptionEntity cartItemOptionEntity WHERE cartItemOptionEntity.cartItem.id = ?1")
    void deleteByCartItemId(Long cartItemId);

    @Modifying
    @Query("DELETE FROM CartItemOptionEntity cartItemOptionEntity WHERE cartItemOptionEntity.optionDetail.id = ?1")
    void deleteByOptionDetailId(Long optionDetailId);

    @Query(
        "SELECT cartItemOptionEntity FROM CartItemOptionEntity cartItemOptionEntity" +
        " WHERE cartItemOptionEntity.cartItem.id = ?1" +
        " AND cartItemOptionEntity.optionDetail.id IN ?2"
    )
    Set<CartItemOptionEntity> findByCartItemIdAndOptionDetailIds(Long cartItemId, Set<Long> optionDetailIds);

    @Query(
        "SELECT cartItemOptionEntity FROM CartItemOptionEntity cartItemOptionEntity" +
        " WHERE cartItemOptionEntity.optionDetail.id = ?1" +
        " AND cartItemOptionEntity.cartItem.id IN ?2"
    )
    Set<CartItemOptionEntity> findByOptionDetailIdAndCartItemIds(Long optionDetailId, Set<Long> cartItemIds);
}
