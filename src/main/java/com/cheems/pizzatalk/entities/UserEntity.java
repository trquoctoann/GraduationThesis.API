package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.constant.LoginConstants;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "user")
public class UserEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Pattern(regexp = LoginConstants.LOGIN_REGEX)
    @Size(max = 20)
    @Column(name = "username", unique = true, length = 20, nullable = false)
    private String username;

    @NotNull
    @Size(max = 60)
    @Column(name = "password", length = 60, nullable = false)
    @JsonIgnore
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @NotNull
    @Size(min = 5, max = 254)
    @Column(name = "email", length = 254, unique = true, nullable = false)
    private String email;

    @Size(max = 300)
    @Column(name = "image_url", length = 300)
    private String imageURL;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @NotNull
    @Size(max = 5)
    @Column(name = "lang_key", length = 5, nullable = false)
    private String langKey;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = { "user", "role" }, allowSetters = true)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<UserKeyEntity> userKeys = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserEntity username(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity password(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserEntity firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserEntity lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity email(String email) {
        this.email = email;
        return this;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public UserEntity imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserEntity status(UserStatus status) {
        this.status = status;
        return this;
    }

    public String getLangKey() {
        return this.langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public UserEntity langKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public Set<UserRoleEntity> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRoleEntity> userRoles) {
        if (this.userRoles != null) {
            this.userRoles.forEach(userRole -> userRole.setUser(null));
        }
        if (userRoles != null) {
            userRoles.forEach(userRole -> userRole.setUser(this));
        }
        this.userRoles = userRoles;
    }

    public UserEntity userRoles(Set<UserRoleEntity> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public UserEntity addUserRole(UserRoleEntity userRole) {
        userRole.setUser(this);
        this.userRoles.add(userRole);
        return this;
    }

    public UserEntity removeUserRole(UserRoleEntity userRole) {
        userRole.setUser(null);
        this.userRoles.remove(userRole);
        return this;
    }

    public Set<UserKeyEntity> getUserKeys() {
        return this.userKeys;
    }

    public void setUserKeys(Set<UserKeyEntity> userKeys) {
        if (this.userKeys != null) {
            this.userKeys.forEach(userKey -> userKey.setUser(null));
        }
        if (userKeys != null) {
            userKeys.forEach(userKey -> userKey.setUser(this));
        }
        this.userKeys = userKeys;
    }

    public UserEntity userKeys(Set<UserKeyEntity> userKeys) {
        this.setUserKeys(userKeys);
        return this;
    }

    public UserEntity addUserKey(UserKeyEntity userKey) {
        userKey.setUser(this);
        this.userKeys.add(userKey);
        return this;
    }

    public UserEntity removeUserKey(UserKeyEntity userKey) {
        userKey.setUser(null);
        this.userKeys.remove(userKey);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        return id != null && id.equals(((UserEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "User{" +
            "id=" +
            getId() +
            ", username='" +
            getUsername() +
            "'" +
            ", firstName='" +
            getFirstName() +
            "'" +
            ", lastName='" +
            getLastName() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", imageURL='" +
            getImageURL() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", langKey='" +
            getLangKey() +
            "'" +
            "}"
        );
    }
}
