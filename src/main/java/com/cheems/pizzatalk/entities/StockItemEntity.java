package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "stock_item")
public class StockItemEntity implements Serializable {

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
    @Size(min = 6, max = 6)
    @Column(name = "sku", unique = true, nullable = false, length = 6)
    private String sku;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Column(name = "total_quantity", nullable = false)
    private Long totalQuantity;

    @NotNull
    @Column(name = "reorder_level", nullable = false)
    private Long reorderLevel;

    @NotNull
    @Column(name = "reorder_quantity", nullable = false)
    private Long reorderQuantity;

    @NotNull
    @Column(name = "selling_price", nullable = false)
    private Float sellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stockItems", "area" }, allowSetters = true)
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category" }, allowSetters = true)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "option", "productOptions", "stockItems" }, allowSetters = true)
    private OptionDetailEntity optionDetail;

    @OneToMany(mappedBy = "stockItem")
    @JsonIgnoreProperties(value = { "stockItem" }, allowSetters = true)
    private Set<StockBatchEntity> stockBatches = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockItemEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockItemEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public StockItemEntity sku(String sku) {
        this.sku = sku;
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public StockItemEntity unit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public Long getTotalQuantity() {
        return this.totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public StockItemEntity totalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public Long getReorderLevel() {
        return this.reorderLevel;
    }

    public void setReorderLevel(Long reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public StockItemEntity reorderLevel(Long reorderLevel) {
        this.reorderLevel = reorderLevel;
        return this;
    }

    public Long getReorderQuantity() {
        return this.reorderQuantity;
    }

    public void setReorderQuantity(Long reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public StockItemEntity reorderQuantity(Long reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
        return this;
    }

    public Float getSellingPrice() {
        return this.sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public StockItemEntity sellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public StoreEntity getStore() {
        return this.store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public StockItemEntity store(StoreEntity store) {
        this.store = store;
        return this;
    }

    public ProductEntity getProduct() {
        return this.product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public StockItemEntity product(ProductEntity product) {
        this.product = product;
        return this;
    }

    public OptionDetailEntity getOptionDetail() {
        return this.optionDetail;
    }

    public void setOptionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
    }

    public StockItemEntity optionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
        return this;
    }

    public Set<StockBatchEntity> getStockBatches() {
        return this.stockBatches;
    }

    public void setStockBatches(Set<StockBatchEntity> stockBatches) {
        if (this.stockBatches != null) {
            this.stockBatches.forEach(stockBatch -> stockBatch.setStockItem(null));
        }
        if (stockBatches != null) {
            stockBatches.forEach(stockBatch -> stockBatch.setStockItem(this));
        }
        this.stockBatches = stockBatches;
    }

    public StockItemEntity stockBatches(Set<StockBatchEntity> stockBatches) {
        this.setStockBatches(stockBatches);
        return this;
    }

    public StockItemEntity addStockBatch(StockBatchEntity stockBatch) {
        stockBatch.setStockItem(this);
        this.stockBatches.add(stockBatch);
        return this;
    }

    public StockItemEntity removeStockBatch(StockBatchEntity stockBatch) {
        stockBatch.setStockItem(null);
        this.stockBatches.remove(stockBatch);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItemEntity)) {
            return false;
        }
        return id != null && id.equals(((StockItemEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "StockItem{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", sku='" +
            getSku() +
            "'" +
            ", unit='" +
            getUnit() +
            "'" +
            ", totalQuantity='" +
            getTotalQuantity() +
            "'" +
            ", reorderLevel='" +
            getReorderLevel() +
            "'" +
            ", reorderQuantity='" +
            getReorderQuantity() +
            "'" +
            ", sellingPrice='" +
            getSellingPrice() +
            "'" +
            "}"
        );
    }
}
