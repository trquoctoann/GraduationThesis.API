package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "category")
public class CategoryEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "original_id")
    private Long originalId;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommerceStatus status;

    @Size(max = 300)
    @Column(name = "image_path", length = 300)
    private String imagePath;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "parentProduct", "productVariations", "productOptions", "category" }, allowSetters = true)
    private Set<ProductEntity> products;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Long getOriginalId() {
        return this.originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public CategoryEntity originalId(Long originalId) {
        this.originalId = originalId;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryEntity description(String description) {
        this.description = description;
        return this;
    }

    public CommerceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CommerceStatus status) {
        this.status = status;
    }

    public CategoryEntity status(CommerceStatus status) {
        this.status = status;
        return this;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CategoryEntity imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Set<ProductEntity> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductEntity> products) {
        if (this.products != null) {
            this.products.forEach(product -> product.setCategory(null));
        }
        if (products != null) {
            products.forEach(product -> product.setCategory(this));
        }
        this.products = products;
    }

    public CategoryEntity products(Set<ProductEntity> products) {
        this.setProducts(products);
        return this;
    }

    public CategoryEntity addProduct(ProductEntity product) {
        product.setCategory(this);
        this.products.add(product);
        return this;
    }

    public CategoryEntity removeProduct(ProductEntity product) {
        product.setCategory(null);
        this.products.remove(product);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryEntity)) {
            return false;
        }
        return id != null && id.equals(((CategoryEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Category{" +
            "id=" +
            getId() +
            ", originalId='" +
            getOriginalId() +
            "'" +
            ", name='" +
            getName() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", imagePath='" +
            getImagePath() +
            "'" +
            "}"
        );
    }
}
