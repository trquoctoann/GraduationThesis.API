package com.cheems.pizzatalk.modules.cartitem.adapter.api;

import com.cheems.pizzatalk.entities.mapper.CartItemMapper;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.CreateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.UpdateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.query.CartItemCriteria;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemLifecycleUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.QueryCartItemUseCase;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CartItemResource {

    private final Logger log = LoggerFactory.getLogger(CartItemResource.class);

    private static final String ENTITY_NAME = "cartItem";

    @Value("${spring.application.name}")
    private String applicationName;

    private final CartItemLifecycleUseCase cartItemLifecycleUseCase;

    private final QueryCartItemUseCase queryCartItemUseCase;

    public CartItemResource(CartItemLifecycleUseCase cartItemLifecycleUseCase, QueryCartItemUseCase queryCartItemUseCase) {
        this.cartItemLifecycleUseCase = cartItemLifecycleUseCase;
        this.queryCartItemUseCase = queryCartItemUseCase;
    }

    @GetMapping("/cart-items/all")
    public ResponseEntity<List<CartItem>> getAllCartItems(CartItemCriteria criteria) {
        log.debug("REST request to get all cart items by criteria: {}", criteria);
        criteria.addFetchAttribute(CartItemMapper.DOMAIN_PRODUCT);
        criteria.addFetchAttribute(CartItemMapper.DOMAIN_OPTION_DETAILS);
        return ResponseEntity.ok().body(queryCartItemUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/cart-items")
    public ResponseEntity<List<CartItem>> getPageCartItems(CartItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get cart items by criteria: {}", criteria);
        criteria.addFetchAttribute(CartItemMapper.DOMAIN_PRODUCT);
        Page<CartItem> page = queryCartItemUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/cart-items/{id}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get cart item, ID: {}", id);
        Optional<CartItem> optionalCartItem = queryCartItemUseCase.findById(
            id,
            CartItemMapper.DOMAIN_CART,
            CartItemMapper.DOMAIN_PRODUCT,
            CartItemMapper.DOMAIN_OPTION_DETAILS
        );
        return optionalCartItem
            .map(cartItem -> ResponseEntity.ok().body(cartItem))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/cart-items")
    public ResponseEntity<Void> createCartItem(@Valid @RequestBody CreateCartItemCommand command) throws URISyntaxException {
        log.debug("REST request to create cart item: {}", command);
        CartItem cartItem = cartItemLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/cart-items/" + cartItem.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + cartItem.getId().toString());

        return ResponseEntity.created(new URI("/api/cart-items/" + cartItem.getId())).headers(headers).build();
    }

    @PutMapping("/cart-items/{id}")
    public ResponseEntity<Void> updateCartItem(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateCartItemCommand command
    ) {
        log.debug("REST request to update cart item, ID: {}", id);
        CartItem cartItem = cartItemLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + cartItem.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/cart-items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete cart item, ID: {}", id);
        cartItemLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
