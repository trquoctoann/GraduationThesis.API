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

    private StringFilter value;

    private StringFilter sku;

    private StringFilter code;

    private StringFilter uomId;

    private CommerceStatusFilter status;

    private RangeFilter<Float> price;

    private RangeFilter<Long> quantity;

    private RangeFilter<Long> optionId;

    public OptionDetailCriteria() {}

    public OptionDetailCriteria(OptionDetailCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.uomId = other.uomId == null ? null : other.uomId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.optionId = other.optionId == null ? null : other.optionId.copy();
    }

    @Override
    public OptionDetailCriteria copy() {
        return new OptionDetailCriteria(this);
    }
}
