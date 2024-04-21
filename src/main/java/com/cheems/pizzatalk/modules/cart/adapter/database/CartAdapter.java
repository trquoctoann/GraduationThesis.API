package com.cheems.pizzatalk.modules.cart.adapter.database;

import com.cheems.pizzatalk.entities.mapper.CartMapper;
import com.cheems.pizzatalk.modules.cart.application.port.out.CartPort;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import com.cheems.pizzatalk.repository.CartRepository;
import org.springframework.stereotype.Component;

@Component
public class CartAdapter implements CartPort {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    public CartAdapter(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Cart save(Cart cart) {
        return cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cart)));
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
