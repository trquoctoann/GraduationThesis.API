package com.cheems.pizzatalk.modules.cart.adapter.api;

import com.cheems.pizzatalk.entities.mapper.CartMapper;
import com.cheems.pizzatalk.modules.cart.application.port.in.query.CartCriteria;
import com.cheems.pizzatalk.modules.cart.application.port.in.share.CartLifecycleUseCase;
import com.cheems.pizzatalk.modules.cart.application.port.in.share.QueryCartUseCase;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private final CartLifecycleUseCase cartLifecycleUseCase;

    private final QueryCartUseCase queryCartUseCase;

    public CartResource(CartLifecycleUseCase cartLifecycleUseCase, QueryCartUseCase queryCartUseCase) {
        this.cartLifecycleUseCase = cartLifecycleUseCase;
        this.queryCartUseCase = queryCartUseCase;
    }

    @GetMapping("/carts/all")
    public ResponseEntity<List<Cart>> getAllCarts(CartCriteria criteria) {
        log.debug("REST request to get all carts by criteria: {}", criteria);
        criteria.addFetchAttribute(CartMapper.DOMAIN_CART_ITEMS);
        return ResponseEntity.ok().body(queryCartUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/carts")
    public ResponseEntity<List<Cart>> getPageCarts(CartCriteria criteria, Pageable pageable) {
        log.debug("REST request to get carts by criteria: {}", criteria);
        Page<Cart> page = queryCartUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/carts/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get cart, ID: {}", id);
        Optional<Cart> optionalCart = queryCartUseCase.findById(id, CartMapper.DOMAIN_CART_ITEMS, CartMapper.DOMAIN_USER);
        return optionalCart
            .map(cart -> ResponseEntity.ok().body(cart))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/carts/{id}/check-out")
    public ResponseEntity<Void> checkoutCart(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to checkout cart, ID: {}", id);
        cartLifecycleUseCase.checkoutCart(id);
        return ResponseEntity.noContent().build();
    }
}
