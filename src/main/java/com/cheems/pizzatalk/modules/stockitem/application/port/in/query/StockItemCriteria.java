package com.cheems.pizzatalk.modules.stockitem.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.UnitFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class StockItemCriteria extends QuerySelfValidating<StockItemCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter sku;

    private UnitFilter unit;

    private RangeFilter<Long> totalQuantity;

    private RangeFilter<Long> reorderLevel;

    private RangeFilter<Long> reorderQuantity;

    private RangeFilter<Float> sellingPrice;

    private RangeFilter<Long> storeId;

    private RangeFilter<Long> productId;

    private RangeFilter<Long> optionDetailId;

    private RangeFilter<Long> stockBatchId;

    public StockItemCriteria() {}

    public StockItemCriteria(StockItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.totalQuantity = other.totalQuantity == null ? null : other.totalQuantity.copy();
        this.reorderLevel = other.reorderLevel == null ? null : other.reorderLevel.copy();
        this.reorderQuantity = other.reorderQuantity == null ? null : other.reorderQuantity.copy();
        this.sellingPrice = other.sellingPrice == null ? null : other.sellingPrice.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.optionDetailId = other.optionDetailId == null ? null : other.optionDetailId.copy();
        this.stockBatchId = other.stockBatchId == null ? null : other.stockBatchId.copy();
    }

    @Override
    public StockItemCriteria copy() {
        return new StockItemCriteria(this);
    }
}
