package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "cart_item_option")
public class CartItemOptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cart", "product", "cartItemOptions" }, allowSetters = true)
    private CartItemEntity cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "option", "productOptions", "stockItems", "cartItemOptions" }, allowSetters = true)
    private OptionDetailEntity optionDetail;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartItemOptionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public CartItemEntity getCartItem() {
        return this.cartItem;
    }

    public void setCartItem(CartItemEntity cartItem) {
        this.cartItem = cartItem;
    }

    public CartItemOptionEntity cartItem(CartItemEntity cartItem) {
        this.cartItem = cartItem;
        return this;
    }

    public OptionDetailEntity getOptionDetail() {
        return this.optionDetail;
    }

    public void setOptionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
    }

    public CartItemOptionEntity optionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItemOptionEntity)) {
            return false;
        }
        return id != null && id.equals(((CartItemOptionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CartItemOption{" + "id=" + getId() + "}";
    }
}
