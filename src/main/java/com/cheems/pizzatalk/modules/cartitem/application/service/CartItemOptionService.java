package com.cheems.pizzatalk.modules.cartitem.application.service;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.CartItemOptionUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.CartItemPort;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemOptionService implements CartItemOptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(CartItemOptionService.class);

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    private final CartItemPort cartItemPort;

    public CartItemOptionService(QueryOptionDetailUseCase queryOptionDetailUseCase, CartItemPort cartItemPort) {
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
        this.cartItemPort = cartItemPort;
    }

    @Override
    public void saveOptionDetailToCartItem(Long cartItemId, Set<Long> optionDetailIds) {
        log.debug("Saving option detail: {} to cart item id: {}", optionDetailIds, cartItemId);
        List<OptionDetail> optionDetails = queryOptionDetailUseCase.findListByListIds(new ArrayList<>(optionDetailIds));

        Set<Long> requestSaveOptionDetailIds = optionDetails.stream().map(OptionDetail::getId).collect(Collectors.toSet());
        Set<Long> existOptionDetailIds = queryOptionDetailUseCase
            .findListByCartItemId(cartItemId)
            .stream()
            .map(OptionDetail::getId)
            .collect(Collectors.toSet());

        Set<Long> inSaveDemandOptionDetailIds = new HashSet<>(requestSaveOptionDetailIds);
        inSaveDemandOptionDetailIds.removeAll(existOptionDetailIds);

        Set<Long> inRemoveOptionDetailIds = new HashSet<>(existOptionDetailIds);
        inRemoveOptionDetailIds.removeAll(requestSaveOptionDetailIds);

        cartItemPort.removeOptionDetail(cartItemId, inRemoveOptionDetailIds);
        cartItemPort.saveOptionDetail(cartItemId, inSaveDemandOptionDetailIds);

        log.debug("Saved option detail: {} to cart item id: {}", optionDetailIds, cartItemId);
    }

    @Override
    public void removeAllOptionOfCartItem(Long cartItemId) {
        log.debug("Removing all option detail of cart item id: {}", cartItemId);
        cartItemPort.removeAllOptionOfCartItem(cartItemId);
        log.debug("Removed all option detail of cart item id: {}", cartItemId);
    }
}
