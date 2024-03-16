package com.cheems.pizzatalk.modules.stockitem.adapter.api.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadUpdateQuantityStockItem {

    private Long newQuantity;
}
