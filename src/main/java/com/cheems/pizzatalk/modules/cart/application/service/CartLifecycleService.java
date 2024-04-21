package com.cheems.pizzatalk.modules.cart.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import com.cheems.pizzatalk.modules.cart.application.port.in.command.CreateCartCommand;
import com.cheems.pizzatalk.modules.cart.application.port.in.command.UpdateCartCommand;
import com.cheems.pizzatalk.modules.cart.application.port.in.share.CartLifecycleUseCase;
import com.cheems.pizzatalk.modules.cart.application.port.in.share.QueryCartUseCase;
import com.cheems.pizzatalk.modules.cart.application.port.out.CartPort;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartLifecycleService implements CartLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(CartLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final CartPort cartPort;

    private final QueryCartUseCase queryCartUseCase;

    public CartLifecycleService(ObjectMapper objectMapper, CartPort cartPort, QueryCartUseCase queryCartUseCase) {
        this.objectMapper = objectMapper;
        this.cartPort = cartPort;
        this.queryCartUseCase = queryCartUseCase;
    }

    @Override
    public Cart create(CreateCartCommand command) {
        log.debug("Creating cart: {}", command);
        if (queryCartUseCase.findListByStatus(CartStatus.ACTIVE).size() > 0) {
            throw new BusinessException("Current active cart is not checked out yet");
        }

        Cart cart = objectMapper.convertValue(command, Cart.class);
        cart.setStatus(CartStatus.ACTIVE);
        cart = cartPort.save(cart);

        log.debug("Created cart: {}", command);
        return cart;
    }

    @Override
    public Cart update(UpdateCartCommand command) {
        log.debug("Updating cart, id: {}", command.getId());
        Cart existCart = queryCartUseCase.getById(command.getId());

        Cart cart = objectMapper.convertValue(command, Cart.class);
        cart.setId(existCart.getId());
        cart.setStatus(existCart.getStatus());
        cart.setUserId(existCart.getUserId());

        cart = cartPort.save(cart);
        log.debug("Updating cart, id: {}", command.getId());
        return cart;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting cart, id: {}", id);
        cartPort.deleteById(id);
        log.debug("Deleted cart, id: {}", id);
    }

    @Override
    public Cart checkoutCart(Long id) {
        log.debug("Checking out cart, id: {}", id);
        Cart existCart = queryCartUseCase.getById(id);
        existCart.setStatus(CartStatus.CHECKED_OUT);
        existCart = cartPort.save(existCart);
        log.debug("Checked out cart, id: {}", id);
        return existCart;
    }
}
