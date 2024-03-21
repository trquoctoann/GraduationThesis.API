package com.cheems.pizzatalk.modules.product.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.product.application.port.in.share.ProductOptionUseCase;
import com.cheems.pizzatalk.modules.product.application.port.out.ProductPort;

@Service
@Transactional
public class ProductOptionService implements ProductOptionUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ProductOptionService.class);

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    private final ProductPort productPort;

    public ProductOptionService(QueryOptionDetailUseCase queryOptionDetailUseCase, ProductPort productPort) {
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
        this.productPort = productPort;
    }

    @Override
    public void saveOptionToProduct(Long productId, Long optionId, Set<Long> optionDetailIds) {
        log.debug("Saving option detail: {} of option id: {} to product id: {}", optionDetailIds, optionId, productId);
        List<OptionDetail> optionDetails = queryOptionDetailUseCase.findListByListIds(new ArrayList<>(optionDetailIds));

        Set<Long> requestSaveOptionDetailIds = optionDetails.stream().map(OptionDetail::getId).collect(Collectors.toSet());
        Set<Long> existOptionDetail = queryOptionDetailUseCase.findListByProductIdAndOptionId(productId, optionId).stream().map(OptionDetail::getId).collect(Collectors.toSet());

        Set<Long> inSaveDemandOptionDetailIds = new HashSet<>(requestSaveOptionDetailIds);
        inSaveDemandOptionDetailIds.removeAll(existOptionDetail);

        Set<Long> inRemoveDemandOptionDetailIds = new HashSet<>(existOptionDetail);
        inRemoveDemandOptionDetailIds.removeAll(requestSaveOptionDetailIds);

        productPort.removeOptionDetail(productId, optionId, inRemoveDemandOptionDetailIds);
        productPort.saveOptionDetail(productId, optionId, inSaveDemandOptionDetailIds);

        log.debug("Saved option detail: {} of option id: {} to product id: {}", optionDetailIds, optionId, productId);
    }

    @Override
    public void removeAllOptionOfProduct(Long productId) {
        log.debug("Removing all option of product id: {}", productId);
        productPort.removeAllOptionOfProduct(productId);
        log.debug("Removed all option of product id: {}", productId);
    }
}
