package com.cheems.pizzatalk.modules.stockbatch.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.entities.filter.RefillStatusFilter;
import com.cheems.pizzatalk.entities.filter.UnitFilter;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class StockBatchCriteria extends QuerySelfValidating<StockBatchCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private UnitFilter unit;

    private RangeFilter<Float> purchasePricePerUnit;

    private RefillStatusFilter status;

    private RangeFilter<Long> remainingQuantity;

    private RangeFilter<Long> orderedQuantity;

    private RangeFilter<Long> receivedQuantity;

    private Filter<Instant> orderedDate;

    private Filter<Instant> receivedDate;

    private Filter<Instant> expirationDate;

    private RangeFilter<Long> stockItemId;

    public StockBatchCriteria() {}

    public StockBatchCriteria(StockBatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.purchasePricePerUnit = other.purchasePricePerUnit == null ? null : other.purchasePricePerUnit.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.remainingQuantity = other.remainingQuantity == null ? null : other.remainingQuantity.copy();
        this.orderedQuantity = other.orderedQuantity == null ? null : other.orderedQuantity.copy();
        this.receivedQuantity = other.receivedQuantity == null ? null : other.receivedQuantity.copy();
        this.orderedDate = other.orderedDate == null ? null : other.orderedDate.copy();
        this.receivedDate = other.receivedDate == null ? null : other.receivedDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.stockItemId = other.stockItemId == null ? null : other.stockItemId.copy();
    }

    @Override
    public StockBatchCriteria copy() {
        return new StockBatchCriteria(this);
    }
}
