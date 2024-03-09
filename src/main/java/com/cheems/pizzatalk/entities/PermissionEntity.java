package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "permission")
public class PermissionEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "permission")
    @JsonIgnoreProperties(value = { "role", "permission" }, allowSetters = true)
    private Set<RolePermissionEntity> rolePermissions = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionEntity name(String name) {
        this.name = name;
        return this;
    }

    public Set<RolePermissionEntity> getRolePermissions() {
        return this.rolePermissions;
    }

    public void setRolePermissions(Set<RolePermissionEntity> rolePermissions) {
        if (this.rolePermissions != null) {
            this.rolePermissions.forEach(rolePermission -> rolePermission.setPermission(null));
        }
        if (rolePermissions != null) {
            rolePermissions.forEach(rolePermission -> rolePermission.setPermission(this));
        }
        this.rolePermissions = rolePermissions;
    }

    public PermissionEntity rolePermissions(Set<RolePermissionEntity> rolePermissions) {
        this.setRolePermissions(rolePermissions);
        return this;
    }

    public PermissionEntity addRolePermission(RolePermissionEntity rolePermission) {
        rolePermission.setPermission(this);
        this.rolePermissions.add(rolePermission);
        return this;
    }

    public PermissionEntity removeRolePermission(RolePermissionEntity rolePermission) {
        rolePermission.setPermission(null);
        this.rolePermissions.remove(rolePermission);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionEntity)) {
            return false;
        }
        return id != null && id.equals(((PermissionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("Permission{" + "id=" + getId() + ", name='" + getName() + "'" + "}");
    }
}
