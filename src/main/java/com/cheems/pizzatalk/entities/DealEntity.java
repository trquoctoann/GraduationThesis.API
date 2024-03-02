package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.DealStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "deal")
public class DealEntity extends AbstractAuditingEntity {

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
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "status", nullable = false)
    private DealStatus status;

    @Size(max = 10)
    @Column(name = "deal_no", length = 10)
    private String dealNo;

    @Column(name = "price")
    private Float price;

    @NotNull
    @Size(max = 50)
    @Column(name = "slug", length = 50, unique = true, nullable = false)
    private String slug;

    @Size(max = 300)
    @Column(name = "image_path", length = 300)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parentDeal", "dealVariations" }, allowSetters = true)
    private DealEntity parentDeal;

    @OneToMany(mappedBy = "parentDeal")
    @JsonIgnoreProperties(value = { "parentDeal", "dealVariations" }, allowSetters = true)
    private Set<DealEntity> dealVariations = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DealEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DealEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DealEntity description(String description) {
        this.description = description;
        return this;
    }

    public DealStatus getStatus() {
        return this.status;
    }

    public void setStatus(DealStatus status) {
        this.status = status;
    }

    public DealEntity status(DealStatus status) {
        this.status = status;
        return this;
    }

    public String getDealNo() {
        return this.dealNo;
    }

    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    public DealEntity dealNo(String dealNo) {
        this.dealNo = dealNo;
        return this;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public DealEntity price(Float price) {
        this.price = price;
        return this;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public DealEntity slug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public DealEntity imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public DealEntity getParentDeal() {
        return this.parentDeal;
    }

    public void setParentDeal(DealEntity parentDeal) {
        this.parentDeal = parentDeal;
    }

    public DealEntity parentDeal(DealEntity parentDeal) {
        this.parentDeal = parentDeal;
        return this;
    }

    public Set<DealEntity> getDealVariations() {
        return this.dealVariations;
    }

    public void setDealVariations(Set<DealEntity> dealVariations) {
        this.dealVariations = dealVariations;
    }

    public DealEntity dealVariations(Set<DealEntity> dealVariations) {
        this.dealVariations = dealVariations;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealEntity)) {
            return false;
        }
        return id != null && id.equals(((DealEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Deal={" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", dealNo='" +
            getDealNo() +
            "'" +
            ", price='" +
            getPrice() +
            "'" +
            ", slug='" +
            getSlug() +
            "'" +
            ", imagePath='" +
            getImagePath() +
            "'" +
            "}"
        );
    }
}
