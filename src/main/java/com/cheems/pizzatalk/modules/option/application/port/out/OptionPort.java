package com.cheems.pizzatalk.modules.option.application.port.out;

import com.cheems.pizzatalk.modules.option.domain.Option;

public interface OptionPort {
    Option save(Option option);

    void removeAllProductOfOption(Long optionId);

    void deleteById(Long id);
}
