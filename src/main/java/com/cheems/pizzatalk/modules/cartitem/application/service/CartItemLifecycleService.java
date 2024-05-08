package com.cheems.pizzatalk.modules.cartitem.application.service;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.CreateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.UpdateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemLifecycleUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemOptionUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.QueryCartItemUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.CartItemPort;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.QueryStockItemUseCase;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    private final QueryStockItemUseCase queryStockItemUseCase;

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    public CartItemLifecycleService(
        ObjectMapper objectMapper,
        CartItemPort cartItemPort,
        CartItemOptionUseCase cartItemOptionUseCase,
        QueryCartItemUseCase queryCartItemUseCase,
        QueryStockItemUseCase queryStockItemUseCase,
        QueryOptionDetailUseCase queryOptionDetailUseCase
    ) {
        this.objectMapper = objectMapper;
        this.cartItemPort = cartItemPort;
        this.cartItemOptionUseCase = cartItemOptionUseCase;
        this.queryCartItemUseCase = queryCartItemUseCase;
        this.queryStockItemUseCase = queryStockItemUseCase;
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
    }

    @Override
    public CartItem create(CreateCartItemCommand command) {
        log.debug("Creating cart item: {}", command);
        Optional<CartItem> checkCartItem = queryCartItemUseCase.findDuplicateCartItem(command.getCartId(), command.getProductId(), command.getOptionDetailIds());
        if (checkCartItem.isPresent()) {
            CartItem existCartItem = checkCartItem.get();
            Integer newQuantity = existCartItem.getQuantity() + command.getQuantity();
            existCartItem.setPrice(existCartItem.getPrice() / existCartItem.getQuantity() * newQuantity);
            existCartItem.setQuantity(newQuantity);

            existCartItem = cartItemPort.save(existCartItem);
            log.debug("Add {} cart item id: {}", command.getQuantity(), existCartItem.getId());
            return existCartItem;
        }
        CartItem cartItem = objectMapper.convertValue(command, CartItem.class);

        Set<OptionDetail> optionDetails = new HashSet<>(
            queryOptionDetailUseCase.findListByListIds(new ArrayList<>(command.getOptionDetailIds()))
        );
        cartItem.setOptionDetails(optionDetails);
        setCartItemPrice(cartItem);
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
        
        Set<OptionDetail> optionDetails = new HashSet<>(
            queryOptionDetailUseCase.findListByListIds(new ArrayList<>(command.getOptionDetailIds()))
        );
        cartItem.setOptionDetails(optionDetails);
        setCartItemPrice(cartItem);

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

    private CartItem setCartItemPrice(CartItem cartItem) {
        Long storeId = (long) 1;
        StockItem productStockItem = queryStockItemUseCase.findListByStoreIdAndProductId(storeId, cartItem.getProductId()).get(0);

        Float totalPrice = productStockItem.getSellingPrice() * cartItem.getQuantity();
        for (OptionDetail optionDetail : cartItem.getOptionDetails()) {
            if (optionDetail.getOptionId() != 2) continue;
            StockItem optionDetailStockItem = queryStockItemUseCase
                .findListByStoreIdAndOptionDetailId(storeId, optionDetail.getId())
                .get(0);
            totalPrice = totalPrice + optionDetailStockItem.getSellingPrice() * cartItem.getQuantity();
        }
        cartItem.setPrice(totalPrice);
        return cartItem;
    }
}
