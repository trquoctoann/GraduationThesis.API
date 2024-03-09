package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.RolePermissionEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository
    extends JpaRepository<RolePermissionEntity, Long>, JpaSpecificationExecutor<RolePermissionEntity> {
    @Query(
        "SELECT rolePermissionEntity FROM RolePermissionEntity rolePermissionEntity" +
        " WHERE rolePermissionEntity.role.id = ?1" +
        " AND rolePermissionEntity.permission.id IN ?2"
    )
    Set<RolePermissionEntity> findByRoleIdAndPermissionIds(Long roleId, Set<Long> permissionIds);

    @Modifying
    @Query("DELETE FROM RolePermissionEntity rolePermissionEntity WHERE rolePermissionEntity.role.id = ?1")
    void deleteByRoleId(Long roleId);
}
