package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "option_detail")
public class OptionDetailEntity extends AbstractAuditingEntity {

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

    @Size(max = 30)
    @Column(name = "code", length = 30)
    private String code;

    @Size(max = 2)
    @Column(name = "size", nullable = false, length = 2)
    private String size;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommerceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productOptions", "optionDetails" }, allowSetters = true)
    private OptionEntity option;

    @OneToMany(mappedBy = "optionDetail")
    @JsonIgnoreProperties(value = { "product", "option", "optionDetail" }, allowSetters = true)
    private Set<ProductOptionEntity> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "optionDetail")
    @JsonIgnoreProperties(value = { "store", "product", "optionDetail", "stockBatches" }, allowSetters = true)
    private Set<StockItemEntity> stockItems = new HashSet<>();

    @OneToMany(mappedBy = "optionDetail")
    @JsonIgnoreProperties(value = { "cartItem", "optionDetail" }, allowSetters = true)
    private Set<CartItemOptionEntity> cartItemOptions = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionDetailEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionDetailEntity name(String name) {
        this.name = name;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public OptionDetailEntity sku(String sku) {
        this.sku = sku;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OptionDetailEntity code(String code) {
        this.code = code;
        return this;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public OptionDetailEntity size(String size) {
        this.size = size;
        return this;
    }

    public CommerceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CommerceStatus status) {
        this.status = status;
    }

    public OptionDetailEntity status(CommerceStatus status) {
        this.status = status;
        return this;
    }

    public OptionEntity getOption() {
        return this.option;
    }

    public void setOption(OptionEntity option) {
        this.option = option;
    }

    public OptionDetailEntity option(OptionEntity option) {
        this.option = option;
        return this;
    }

    public Set<ProductOptionEntity> getProductOptions() {
        return this.productOptions;
    }

    public void setProductOptions(Set<ProductOptionEntity> productOptions) {
        if (this.productOptions != null) {
            this.productOptions.forEach(productOption -> productOption.setOptionDetail(null));
        }
        if (productOptions != null) {
            productOptions.forEach(productOption -> productOption.setOptionDetail(this));
        }
        this.productOptions = productOptions;
    }

    public OptionDetailEntity productOptions(Set<ProductOptionEntity> productOptions) {
        this.setProductOptions(productOptions);
        return this;
    }

    public OptionDetailEntity addProductOption(ProductOptionEntity productOption) {
        productOption.setOptionDetail(this);
        this.productOptions.add(productOption);
        return this;
    }

    public OptionDetailEntity removeProductOption(ProductOptionEntity productOption) {
        productOption.setOptionDetail(null);
        this.productOptions.remove(productOption);
        return this;
    }

    public Set<StockItemEntity> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockItemEntity> stockItems) {
        if (this.stockItems != null) {
            this.stockItems.forEach(stockItem -> stockItem.setOptionDetail(null));
        }
        if (stockItems != null) {
            stockItems.forEach(stockItem -> stockItem.setOptionDetail(this));
        }
        this.stockItems = stockItems;
    }

    public OptionDetailEntity stockItems(Set<StockItemEntity> stockItems) {
        this.setStockItems(stockItems);
        return this;
    }

    public OptionDetailEntity addStockItem(StockItemEntity stockItem) {
        stockItem.setOptionDetail(this);
        this.stockItems.add(stockItem);
        return this;
    }

    public OptionDetailEntity removeStockItem(StockItemEntity stockItem) {
        stockItem.setOptionDetail(null);
        this.stockItems.remove(stockItem);
        return this;
    }

    public Set<CartItemOptionEntity> getCartItemOptions() {
        return this.cartItemOptions;
    }

    public void setCartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        if (this.cartItemOptions != null) {
            this.cartItemOptions.forEach(cartItemOption -> cartItemOption.setOptionDetail(null));
        }
        if (cartItemOptions != null) {
            cartItemOptions.forEach(cartItemOption -> cartItemOption.setOptionDetail(this));
        }
        this.cartItemOptions = cartItemOptions;
    }

    public OptionDetailEntity cartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        this.setCartItemOptions(cartItemOptions);
        return this;
    }

    public OptionDetailEntity addCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setOptionDetail(this);
        this.cartItemOptions.add(cartItemOption);
        return this;
    }

    public OptionDetailEntity removeCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setOptionDetail(null);
        this.cartItemOptions.remove(cartItemOption);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDetailEntity)) {
            return false;
        }
        return id != null && id.equals(((OptionDetailEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "OptionDetail={" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", sku='" +
            getSku() +
            "'" +
            ", code='" +
            getCode() +
            "'" +
            ", size='" +
            getSize() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            "}"
        );
    }
}
