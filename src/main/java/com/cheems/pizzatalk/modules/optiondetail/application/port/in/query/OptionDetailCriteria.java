package com.cheems.pizzatalk.modules.optiondetail.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.CommerceStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class OptionDetailCriteria extends QuerySelfValidating<OptionDetailCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter sku;

    private StringFilter code;

    private StringFilter size;

    private CommerceStatusFilter status;

    private RangeFilter<Long> productId;

    private RangeFilter<Long> optionId;

    private RangeFilter<Long> stockItemId;

    private RangeFilter<Long> cartItemId;

    public OptionDetailCriteria() {}

    public OptionDetailCriteria(OptionDetailCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.size = other.size == null ? null : other.size.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.optionId = other.optionId == null ? null : other.optionId.copy();
        this.stockItemId = other.stockItemId == null ? null : other.stockItemId.copy();
        this.cartItemId = other.cartItemId == null ? null : other.cartItemId.copy();
    }

    @Override
    public OptionDetailCriteria copy() {
        return new OptionDetailCriteria(this);
    }
}
