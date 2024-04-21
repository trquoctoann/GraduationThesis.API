package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category", "cartItems" },
        allowSetters = true
    )
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productOptions", "optionDetails" }, allowSetters = true)
    private OptionEntity option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "option", "productOptions", "stockItems" }, allowSetters = true)
    private OptionDetailEntity optionDetail;

    @OneToMany(mappedBy = "productOption")
    @JsonIgnoreProperties(value = { "cartItem", "productOption" }, allowSetters = true)
    private Set<CartItemOptionEntity> cartItemOptions = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOptionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ProductEntity getProduct() {
        return this.product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ProductOptionEntity product(ProductEntity product) {
        this.product = product;
        return this;
    }

    public OptionEntity getOption() {
        return this.option;
    }

    public void setOption(OptionEntity option) {
        this.option = option;
    }

    public ProductOptionEntity option(OptionEntity option) {
        this.option = option;
        return this;
    }

    public OptionDetailEntity getOptionDetail() {
        return this.optionDetail;
    }

    public void setOptionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
    }

    public ProductOptionEntity optionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
        return this;
    }

    public Set<CartItemOptionEntity> getCartItemOptions() {
        return this.cartItemOptions;
    }

    public void setCartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        if (this.cartItemOptions != null) {
            this.cartItemOptions.forEach(cartItemOption -> cartItemOption.setProductOption(null));
        }
        if (cartItemOptions != null) {
            cartItemOptions.forEach(cartItemOption -> cartItemOption.setProductOption(this));
        }
        this.cartItemOptions = cartItemOptions;
    }

    public ProductOptionEntity cartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        this.setCartItemOptions(cartItemOptions);
        return this;
    }

    public ProductOptionEntity addCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setProductOption(this);
        this.cartItemOptions.add(cartItemOption);
        return this;
    }

    public ProductOptionEntity removeCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setProductOption(null);
        this.cartItemOptions.remove(cartItemOption);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionEntity)) {
            return false;
        }
        return id != null && id.equals(((ProductOptionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductOption{" + "id=" + getId() + "}";
    }
}
