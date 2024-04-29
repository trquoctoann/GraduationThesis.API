package com.cheems.pizzatalk.modules.cartitem.application.service;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.CreateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.UpdateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemLifecycleUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemOptionUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.QueryCartItemUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.CartItemPort;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemLifecycleService implements CartItemLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(CartItemLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final CartItemPort cartItemPort;

    private final CartItemOptionUseCase cartItemOptionUseCase;

    private final QueryCartItemUseCase queryCartItemUseCase;

    public CartItemLifecycleService(
        ObjectMapper objectMapper,
        CartItemPort cartItemPort,
        CartItemOptionUseCase cartItemOptionUseCase,
        QueryCartItemUseCase queryCartItemUseCase
    ) {
        this.objectMapper = objectMapper;
        this.cartItemPort = cartItemPort;
        this.cartItemOptionUseCase = cartItemOptionUseCase;
        this.queryCartItemUseCase = queryCartItemUseCase;
    }

    @Override
    public CartItem create(CreateCartItemCommand command) {
        log.debug("Creating cart item: {}", command);
        CartItem cartItem = objectMapper.convertValue(command, CartItem.class);

        cartItem = cartItemPort.save(cartItem);
        cartItemOptionUseCase.saveOptionDetailToCartItem(cartItem.getId(), command.getOptionDetailIds());

        log.debug("Created cart item: {}", command);
        return cartItem;
    }

    @Override
    public CartItem update(UpdateCartItemCommand command) {
        log.debug("Updating cart item, id: {}", command.getId());
        CartItem existCartItem = queryCartItemUseCase.getById(command.getId());

        CartItem cartItem = objectMapper.convertValue(command, CartItem.class);
        cartItem.setId(existCartItem.getId());
        cartItem.setCartId(existCartItem.getCartId());
        cartItem.setProductId(existCartItem.getProductId());

        cartItem = cartItemPort.save(cartItem);
        cartItemOptionUseCase.saveOptionDetailToCartItem(cartItem.getId(), command.getOptionDetailIds());

        log.debug("Updated cart item, id: {}", command.getId());
        return cartItem;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting cart item, id: {}", id);
        cartItemOptionUseCase.removeAllOptionOfCartItem(id);
        cartItemPort.deleteById(id);
        log.debug("Deleted cart item, id: {}", id);
    }
}
