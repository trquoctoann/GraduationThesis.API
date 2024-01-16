package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles", "userKeys" }, allowSetters = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles" }, allowSetters = true)
    private RoleEntity role;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRoleEntity id(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserRoleEntity user(UserEntity user) {
        this.setUser(user);
        return this;
    }

    public RoleEntity getRole() {
        return this.role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public UserRoleEntity role(RoleEntity role) {
        this.setRole(role);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRoleEntity)) {
            return false;
        }
        return id != null && id.equals(((UserRoleEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserRole{" + "id=" + getId() + "}";
    }
}
