package com.cheems.pizzatalk.modules.optiondetail.application.port.out;

import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;

public interface OptionDetailPort {
    OptionDetail save(OptionDetail optionDetail);

    void removeAllProductOfOptionDetail(Long optionDetailId);

    void deleteById(Long id);
}
