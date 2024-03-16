package com.cheems.pizzatalk.modules.stockitem.domain;

import com.cheems.pizzatalk.entities.enumeration.Unit;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockItem implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(min = 6, max = 6)
    private String sku;

    @NotNull
    private Unit unit;

    private Long totalQuantity;

    @NotNull
    private Long reorderLevel;

    @NotNull
    private Long reorderQuantity;

    @NotNull
    private Float sellingPrice;

    @NotNull
    private Long storeId;

    private Store store;

    private Long productId;

    private Product product;

    private Long optionDetailId;

    private OptionDetail optionDetail;

    private Set<StockBatch> stockBatches;
}
