package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

@Entity
@Table(name = "role_permission")
public class RolePermissionEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles", "rolePermissions" }, allowSetters = true)
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rolePermissions" }, allowSetters = true)
    private PermissionEntity permission;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RolePermissionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public RoleEntity getRole() {
        return this.role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public RolePermissionEntity role(RoleEntity role) {
        this.role = role;
        return this;
    }

    public PermissionEntity getPermission() {
        return this.permission;
    }

    public void setPermission(PermissionEntity permission) {
        this.permission = permission;
    }

    public RolePermissionEntity permission(PermissionEntity permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermissionEntity)) {
            return false;
        }
        return id != null && id.equals(((RolePermissionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RolePermission{" + getId() + "}";
    }
}
