package com.cheems.pizzatalk.modules.option.application.service;

import com.cheems.pizzatalk.modules.option.application.port.in.share.OptionProductUseCase;
import com.cheems.pizzatalk.modules.option.application.port.out.OptionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionProductService implements OptionProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(OptionProductService.class);

    private final OptionPort optionPort;

    public OptionProductService(OptionPort optionPort) {
        this.optionPort = optionPort;
    }

    @Override
    public void removeAllProductOfOption(Long optionId) {
        log.debug("Removing all product of option, id: {}", optionId);
        optionPort.removeAllProductOfOption(optionId);
        log.debug("Removed all product of option, id: {}", optionId);
    }
}
