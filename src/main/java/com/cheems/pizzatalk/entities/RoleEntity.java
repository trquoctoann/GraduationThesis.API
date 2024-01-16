package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "role")
public class RoleEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "authority", length = 50, nullable = false)
    private String authority;

    @OneToMany(mappedBy = "role")
    @JsonIgnoreProperties(value = { "user", "role" }, allowSetters = true)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public RoleEntity authority(String authority) {
        this.authority = authority;
        return this;
    }

    public Set<UserRoleEntity> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRoleEntity> userRoles) {
        if (this.userRoles != null) {
            this.userRoles.forEach(userRole -> userRole.setRole(null));
        }
        if (userRoles != null) {
            userRoles.forEach(userRole -> userRole.setRole(this));
        }
        this.userRoles = userRoles;
    }

    public RoleEntity userRoles(Set<UserRoleEntity> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public RoleEntity addUserRole(UserRoleEntity userRole) {
        userRole.setRole(this);
        this.userRoles.add(userRole);
        return this;
    }

    public RoleEntity removeUserRole(UserRoleEntity userRole) {
        userRole.setRole(null);
        this.userRoles.remove(userRole);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleEntity)) {
            return false;
        }
        return id != null && id.equals(((RoleEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("Role{" + "id=" + getId() + ", authority='" + getAuthority() + "'" + "}");
    }
}
