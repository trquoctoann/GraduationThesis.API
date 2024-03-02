package com.cheems.pizzatalk.modules.deal.application.port.out;

import com.cheems.pizzatalk.modules.deal.domain.Deal;

public interface DealPort {
    Deal save(Deal deal);

    void deleteById(Long id);
}
