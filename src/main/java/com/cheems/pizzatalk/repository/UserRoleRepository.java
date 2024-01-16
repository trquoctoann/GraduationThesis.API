package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.UserRoleEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>, JpaSpecificationExecutor<UserRoleEntity> {
    @Modifying
    @Query("DELETE FROM UserRoleEntity userRoleEntity WHERE userRoleEntity.role.id = ?1")
    void deleteByRoleId(Long roleId);

    @Modifying
    @Query("DELETE FROM UserRoleEntity userRoleEntity WHERE userRoleEntity.user.id = ?1")
    void deleteByUserId(Long userId);

    @Query(
        "SELECT userRoleEntity FROM UserRoleEntity userRoleEntity" +
        " WHERE userRoleEntity.role.id = ?1" +
        " AND userRoleEntity.user.id IN ?2"
    )
    Set<UserRoleEntity> findByRoleIdAndUserIds(Long roleId, Set<Long> userIds);

    @Query(
        "SELECT userRoleEntity FROM UserRoleEntity userRoleEntity" +
        " WHERE userRoleEntity.user.id = ?1" +
        " AND userRoleEntity.role.id IN ?2"
    )
    Set<UserRoleEntity> findByUserIdAndRoleIds(Long userId, Set<Long> roleIds);
}
