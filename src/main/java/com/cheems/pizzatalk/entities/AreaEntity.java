package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "area")
public class AreaEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 5)
    @Column(name = "code", length = 5, unique = true, nullable = false)
    private String code;

    @NotNull
    @Size(max = 20)
    @Column(name = "brand_code", length = 20, nullable = false)
    private String brandCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperationalStatus status;

    @NotNull
    @Column(name = "store_count", nullable = false)
    private Long storeCount;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(name = "price_group_id", length = 5, nullable = false)
    private String priceGroupId;

    @OneToMany(mappedBy = "area")
    @JsonIgnoreProperties(value = { "stockItems", "area" }, allowSetters = true)
    private Set<StoreEntity> stores = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AreaEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AreaEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AreaEntity code(String code) {
        this.code = code;
        return this;
    }

    public String getBrandCode() {
        return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public AreaEntity brandCode(String brandCode) {
        this.brandCode = brandCode;
        return this;
    }

    public OperationalStatus getStatus() {
        return this.status;
    }

    public void setStatus(OperationalStatus status) {
        this.status = status;
    }

    public AreaEntity status(OperationalStatus status) {
        this.status = status;
        return this;
    }

    public Long getStoreCount() {
        return this.storeCount;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }

    public AreaEntity storeCount(Long storeCount) {
        this.storeCount = storeCount;
        return this;
    }

    public String getPriceGroupId() {
        return this.priceGroupId;
    }

    public void setPriceGroupId(String priceGroupId) {
        this.priceGroupId = priceGroupId;
    }

    public AreaEntity priceGroupId(String priceGroupId) {
        this.priceGroupId = priceGroupId;
        return this;
    }

    public Set<StoreEntity> getStores() {
        return this.stores;
    }

    public void setStores(Set<StoreEntity> stores) {
        if (this.stores != null) {
            this.stores.forEach(store -> store.setArea(null));
        }
        if (stores != null) {
            stores.forEach(store -> store.setArea(this));
        }
        this.stores = stores;
    }

    public AreaEntity stores(Set<StoreEntity> stores) {
        this.setStores(stores);
        return this;
    }

    public AreaEntity addStore(StoreEntity store) {
        store.setArea(this);
        this.stores.add(store);
        return this;
    }

    public AreaEntity removeStore(StoreEntity store) {
        store.setArea(null);
        this.stores.remove(store);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaEntity)) {
            return false;
        }
        return id != null && id.equals(((AreaEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Area{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", code='" +
            getCode() +
            "'" +
            ", brandCode='" +
            getBrandCode() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", storeCount='" +
            getStoreCount() +
            "'" +
            ", priceGroupId='" +
            getPriceGroupId() +
            "'" +
            "}"
        );
    }
}
