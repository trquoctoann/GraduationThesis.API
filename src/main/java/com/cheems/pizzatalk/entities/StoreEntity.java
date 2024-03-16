package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "store")
public class StoreEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NotNull
    @Size(max = 1000)
    @Column(name = "address", length = 1000, nullable = false)
    private String address;

    @NotNull
    @Size(max = 15)
    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @Email
    @Size(min = 5, max = 254)
    @Column(name = "email", length = 254, nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperationalStatus status;

    @NotNull
    @Column(name = "allow_delivery", nullable = false)
    private Boolean allowDelivery;

    @NotNull
    @Column(name = "allow_pickup", nullable = false)
    private Boolean allowPickup;

    @Size(min = 2, max = 2)
    @Column(name = "country", length = 2)
    private String country;

    @Size(max = 5)
    @Column(name = "state", length = 5)
    private String state;

    @Size(max = 5)
    @Column(name = "district", length = 5)
    private String district;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Size(max = 20)
    @Column(name = "opening_hour", length = 20)
    private String openingHour;

    @Size(max = 300)
    @Column(name = "image_path", length = 300)
    private String imagePath;

    @OneToMany(mappedBy = "store")
    @JsonIgnoreProperties(value = { "store", "product", "optionDetail", "stockBatches" }, allowSetters = true)
    private Set<StockItemEntity> stockItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stores" }, allowSetters = true)
    private AreaEntity area;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StoreEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StoreEntity address(String address) {
        this.address = address;
        return this;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StoreEntity phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StoreEntity email(String email) {
        this.email = email;
        return this;
    }

    public OperationalStatus getStatus() {
        return this.status;
    }

    public void setStatus(OperationalStatus status) {
        this.status = status;
    }

    public StoreEntity status(OperationalStatus status) {
        this.status = status;
        return this;
    }

    public Boolean getAllowDelivery() {
        return this.allowDelivery;
    }

    public void setAllowDelivery(Boolean allowDelivery) {
        this.allowDelivery = allowDelivery;
    }

    public StoreEntity allowDelivery(Boolean allowDelivery) {
        this.allowDelivery = allowDelivery;
        return this;
    }

    public Boolean getAllowPickup() {
        return this.allowPickup;
    }

    public void setAllowPickup(Boolean allowPickup) {
        this.allowPickup = allowPickup;
    }

    public StoreEntity allowPickup(Boolean allowPickup) {
        this.allowPickup = allowPickup;
        return this;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public StoreEntity country(String country) {
        this.country = country;
        return this;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public StoreEntity state(String state) {
        this.state = state;
        return this;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public StoreEntity district(String district) {
        this.district = district;
        return this;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public StoreEntity longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public StoreEntity latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getOpeningHour() {
        return this.openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public StoreEntity openingHour(String openingHour) {
        this.openingHour = openingHour;
        return this;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public StoreEntity imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Set<StockItemEntity> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockItemEntity> stockItems) {
        if (this.stockItems != null) {
            this.stockItems.forEach(stockItem -> stockItem.setStore(null));
        }
        if (stockItems != null) {
            stockItems.forEach(stockItem -> stockItem.setStore(this));
        }
        this.stockItems = stockItems;
    }

    public StoreEntity stockItems(Set<StockItemEntity> stockItems) {
        this.setStockItems(stockItems);
        return this;
    }

    public StoreEntity addStockItem(StockItemEntity stockItem) {
        stockItem.setStore(this);
        this.stockItems.add(stockItem);
        return this;
    }

    public StoreEntity removeStockItem(StockItemEntity stockItem) {
        stockItem.setStore(null);
        this.stockItems.remove(stockItem);
        return this;
    }

    public AreaEntity getArea() {
        return this.area;
    }

    public void setArea(AreaEntity area) {
        this.area = area;
    }

    public StoreEntity area(AreaEntity area) {
        this.area = area;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreEntity)) {
            return false;
        }
        return id != null && id.equals(((StoreEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Store{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", address='" +
            getAddress() +
            "'" +
            ", phoneNumber='" +
            getPhoneNumber() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", allowDelivery='" +
            getAllowDelivery() +
            "'" +
            ", allowPickup='" +
            getAllowPickup() +
            "'" +
            ", country='" +
            getCountry() +
            "'" +
            ", state='" +
            getState() +
            "'" +
            ", district='" +
            getDistrict() +
            "'" +
            ", longitude='" +
            getLongitude() +
            "'" +
            ", latitude='" +
            getLatitude() +
            "'" +
            ", openingHour='" +
            getOpeningHour() +
            "'" +
            ", imagePath='" +
            getImagePath() +
            "'" +
            "}"
        );
    }
}
