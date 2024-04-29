package com.cheems.pizzatalk.modules.cartitem.adapter.database;

import com.cheems.pizzatalk.entities.CartItemOptionEntity;
import com.cheems.pizzatalk.entities.mapper.CartItemMapper;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.CartItemPort;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import com.cheems.pizzatalk.repository.CartItemOptionRepository;
import com.cheems.pizzatalk.repository.CartItemRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CartItemAdapter implements CartItemPort {

    private final CartItemRepository cartItemRepository;

    private final CartItemOptionRepository cartItemOptionRepository;

    private final CartItemMapper cartItemMapper;

    private final OptionDetailMapper optionDetailMapper;

    public CartItemAdapter(
        CartItemRepository cartItemRepository,
        CartItemOptionRepository cartItemOptionRepository,
        CartItemMapper cartItemMapper,
        OptionDetailMapper optionDetailMapper
    ) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemOptionRepository = cartItemOptionRepository;
        this.cartItemMapper = cartItemMapper;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemMapper.toDomain(cartItemRepository.save(cartItemMapper.toEntity(cartItem)));
    }

    @Override
    public void saveOptionDetail(Long cartItemId, Set<Long> optionDetailIds) {
        optionDetailIds.forEach(optionDetailId -> {
            CartItemOptionEntity cartItemOptionEntity = new CartItemOptionEntity();
            cartItemOptionEntity.setCartItem(cartItemMapper.fromId(cartItemId));
            cartItemOptionEntity.setOptionDetail(optionDetailMapper.fromId(optionDetailId));
            cartItemOptionRepository.save(cartItemOptionEntity);
        });
    }

    @Override
    public void removeOptionDetail(Long cartItemId, Set<Long> optionDetailIds) {
        Set<CartItemOptionEntity> existCartItemOptionEntities = cartItemOptionRepository.findByCartItemIdAndOptionDetailIds(
            cartItemId,
            optionDetailIds
        );
        cartItemOptionRepository.deleteAll(existCartItemOptionEntities);
    }

    @Override
    public void removeAllOptionOfCartItem(Long cartItemId) {
        cartItemOptionRepository.deleteByCartItemId(cartItemId);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
