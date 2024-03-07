package com.cheems.pizzatalk.modules.optiondetail.application.port.out;

import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;

public interface OptionDetailPort {
    OptionDetail save(OptionDetail optionDetail);

    void deleteById(Long id);
}
