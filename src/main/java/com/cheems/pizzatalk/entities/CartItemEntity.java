package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "cart_item")
public class CartItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "cartItems" }, allowSetters = true)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category", "cartItems" },
        allowSetters = true
    )
    private ProductEntity product;

    @OneToMany(mappedBy = "cartItem")
    @JsonIgnoreProperties(value = { "cartItem", "optionDetail" }, allowSetters = true)
    private Set<CartItemOptionEntity> cartItemOptions = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartItemEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItemEntity quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public CartItemEntity price(Float price) {
        this.price = price;
        return this;
    }

    public CartEntity getCart() {
        return this.cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public CartItemEntity cart(CartEntity cart) {
        this.cart = cart;
        return this;
    }

    public ProductEntity getProduct() {
        return this.product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public CartItemEntity product(ProductEntity product) {
        this.product = product;
        return this;
    }

    public Set<CartItemOptionEntity> getCartItemOptions() {
        return this.cartItemOptions;
    }

    public void setCartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        if (this.cartItemOptions != null) {
            this.cartItemOptions.forEach(cartItemOption -> cartItemOption.setCartItem(null));
        }
        if (cartItemOptions != null) {
            cartItemOptions.forEach(cartItemOption -> cartItemOption.setCartItem(this));
        }
        this.cartItemOptions = cartItemOptions;
    }

    public CartItemEntity cartItemOptions(Set<CartItemOptionEntity> cartItemOptions) {
        this.setCartItemOptions(cartItemOptions);
        return this;
    }

    public CartItemEntity addCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setCartItem(this);
        this.cartItemOptions.add(cartItemOption);
        return this;
    }

    public CartItemEntity removeCartItemOption(CartItemOptionEntity cartItemOption) {
        cartItemOption.setCartItem(null);
        this.cartItemOptions.remove(cartItemOption);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItemEntity)) {
            return false;
        }
        return id != null && id.equals(((CartItemEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("CartItem{" + "id=" + getId() + ", quantity='" + getQuantity() + "'" + ", price='" + getPrice() + "'" + "}");
    }
}
