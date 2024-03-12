package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "user_key")
public class UserKeyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 36)
    @Column(name = "value", unique = true, length = 36, nullable = false)
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserKeyType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserKeyStatus status;

    @Column(name = "creation_date")
    private Instant creationDate = null;

    @Column(name = "used_date")
    private Instant usedDate = null;

    @Column(name = "expiration_date")
    private Instant expirationDate = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles", "userKeys" }, allowSetters = true)
    private UserEntity user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserKeyEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UserKeyEntity value(String value) {
        this.value = value;
        return this;
    }

    public UserKeyType getType() {
        return this.type;
    }

    public void setType(UserKeyType type) {
        this.type = type;
    }

    public UserKeyEntity type(UserKeyType type) {
        this.type = type;
        return this;
    }

    public UserKeyStatus getStatus() {
        return this.status;
    }

    public void setStatus(UserKeyStatus status) {
        this.status = status;
    }

    public UserKeyEntity status(UserKeyStatus status) {
        this.status = status;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public UserKeyEntity creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Instant getUsedDate() {
        return this.usedDate;
    }

    public void setUsedDate(Instant usedDate) {
        this.usedDate = usedDate;
    }

    public UserKeyEntity usedDate(Instant usedDate) {
        this.usedDate = usedDate;
        return this;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserKeyEntity expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserKeyEntity user(UserEntity user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserKeyEntity)) {
            return false;
        }
        return id != null && id.equals(((UserKeyEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "UserKey{" +
            "id=" +
            getId() +
            ", value='" +
            getValue() +
            "'" +
            ", type='" +
            getType() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", creationDate='" +
            getCreationDate() +
            "'" +
            ", usedDate='" +
            getUsedDate() +
            "'" +
            ", expirationDate='" +
            getExpirationDate() +
            "'" +
            "}"
        );
    }
}
