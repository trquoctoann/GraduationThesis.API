package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "product")
public class ProductEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 2)
    @Column(name = "size", length = 2)
    private String size;

    @NotNull
    @Size(max = 50)
    @Column(name = "slug", length = 50, nullable = false)
    private String slug;

    @Column(name = "description")
    private String description;

    @NotNull
    @Size(min = 6, max = 6)
    @Column(name = "sku", length = 6, unique = true, nullable = false)
    private String sku;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommerceStatus status;

    @Size(max = 300)
    @Column(name = "image_path", length = 300)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category" }, allowSetters = true)
    private ProductEntity parentProduct;

    @OneToMany(mappedBy = "parentProduct")
    @JsonIgnoreProperties(value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category" }, allowSetters = true)
    private Set<ProductEntity> productVariations = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product", "option", "productOptionDetails" }, allowSetters = true)
    private Set<ProductOptionEntity> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "store", "product", "optionDetail", "stockBatches" }, allowSetters = true)
    private Set<StockItemEntity> stockItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private CategoryEntity category;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ProductEntity size(String size) {
        this.size = size;
        return this;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ProductEntity slug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductEntity description(String description) {
        this.description = description;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductEntity sku(String sku) {
        this.sku = sku;
        return this;
    }

    public CommerceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CommerceStatus status) {
        this.status = status;
    }

    public ProductEntity status(CommerceStatus status) {
        this.status = status;
        return this;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ProductEntity imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public ProductEntity getParentProduct() {
        return this.parentProduct;
    }

    public void setParentProduct(ProductEntity parentProduct) {
        this.parentProduct = parentProduct;
    }

    public ProductEntity parentProduct(ProductEntity parentProduct) {
        this.parentProduct = parentProduct;
        return this;
    }

    public Set<ProductEntity> getProductVariations() {
        return this.productVariations;
    }

    public void setProductVariations(Set<ProductEntity> productVariations) {
        this.productVariations = productVariations;
    }

    public ProductEntity productVariations(Set<ProductEntity> productVariations) {
        this.productVariations = productVariations;
        return this;
    }

    public Set<ProductOptionEntity> getProductOptions() {
        return this.productOptions;
    }

    public void setProductOptions(Set<ProductOptionEntity> productOptions) {
        if (this.productOptions != null) {
            this.productOptions.forEach(productOption -> productOption.setProduct(null));
        }
        if (productOptions != null) {
            productOptions.forEach(productOption -> productOption.setProduct(this));
        }
        this.productOptions = productOptions;
    }

    public ProductEntity productOptions(Set<ProductOptionEntity> productOptions) {
        this.setProductOptions(productOptions);
        return this;
    }

    public ProductEntity addProductOption(ProductOptionEntity productOption) {
        productOption.setProduct(this);
        this.productOptions.add(productOption);
        return this;
    }

    public ProductEntity removeProductOption(ProductOptionEntity productOption) {
        productOption.setProduct(null);
        this.productOptions.remove(productOption);
        return this;
    }

    public Set<StockItemEntity> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockItemEntity> stockItems) {
        if (this.stockItems != null) {
            this.stockItems.forEach(stockItem -> stockItem.setProduct(null));
        }
        if (stockItems != null) {
            stockItems.forEach(stockItem -> stockItem.setProduct(this));
        }
        this.stockItems = stockItems;
    }

    public ProductEntity stockItems(Set<StockItemEntity> stockItems) {
        this.setStockItems(stockItems);
        return this;
    }

    public ProductEntity addStockItem(StockItemEntity stockItem) {
        stockItem.setProduct(this);
        this.stockItems.add(stockItem);
        return this;
    }

    public ProductEntity removeStockItem(StockItemEntity stockItem) {
        stockItem.setProduct(null);
        this.stockItems.remove(stockItem);
        return this;
    }

    public CategoryEntity getCategory() {
        return this.category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ProductEntity category(CategoryEntity category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductEntity)) {
            return false;
        }
        return id != null && id.equals(((ProductEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Product={" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", size='" +
            getSize() +
            "'" +
            ", slug='" +
            getSlug() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", sku='" +
            getSku() +
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
