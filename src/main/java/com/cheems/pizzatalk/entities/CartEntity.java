package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "cart")
public class CartEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus status;

    @OneToMany(mappedBy = "cart")
    @JsonIgnoreProperties(value = { "cart", "product", "cartItemOptions" }, allowSetters = true)
    private Set<CartItemEntity> cartItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles", "userKeys", "participants", "carts" }, allowSetters = true)
    private UserEntity user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity id(Long id) {
        this.id = id;
        return this;
    }

    public CartStatus getStatus() {
        return this.status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public CartEntity status(CartStatus status) {
        this.status = status;
        return this;
    }

    public Set<CartItemEntity> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(Set<CartItemEntity> cartItems) {
        if (this.cartItems != null) {
            this.cartItems.forEach(cartItem -> cartItem.setCart(null));
        }
        if (cartItems != null) {
            cartItems.forEach(cartItem -> cartItem.setCart(this));
        }
        this.cartItems = cartItems;
    }

    public CartEntity cartItems(Set<CartItemEntity> cartItems) {
        this.setCartItems(cartItems);
        return this;
    }

    public CartEntity addCartItem(CartItemEntity cartItem) {
        cartItem.setCart(this);
        this.cartItems.add(cartItem);
        return this;
    }

    public CartEntity removeCartItem(CartItemEntity cartItem) {
        cartItem.setCart(null);
        this.cartItems.remove(cartItem);
        return this;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CartEntity user(UserEntity user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartEntity)) {
            return false;
        }
        return id != null && id.equals(((CartEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("Cart{" + "id=" + getId() + ", status='" + getStatus() + "'" + "}");
    }
}
