package com.cheems.pizzatalk.modules.optiondetail.application.service;

import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailProductUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.OptionDetailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionDetailProductService implements OptionDetailProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(OptionDetailProductService.class);

    private final OptionDetailPort optionDetailPort;

    public OptionDetailProductService(OptionDetailPort optionDetailPort) {
        this.optionDetailPort = optionDetailPort;
    }

    @Override
    public void removeAllProductOfOptionDetail(Long optionDetailId) {
        log.debug("Removing all products of option detail id: {}", optionDetailId);
        optionDetailPort.removeAllProductOfOptionDetail(optionDetailId);
        log.debug("Removed all products of option detail id: {}", optionDetailId);
    }
}
