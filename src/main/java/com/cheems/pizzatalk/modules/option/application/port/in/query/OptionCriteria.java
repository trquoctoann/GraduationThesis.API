package com.cheems.pizzatalk.modules.option.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
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
public class OptionCriteria extends QuerySelfValidating<OptionCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter code;

    private CommerceStatusFilter status;

    private Filter<Boolean> isMulti;

    private Filter<Boolean> isRequired;

    private RangeFilter<Long> productId;

    private RangeFilter<Long> optionDetailId;

    public OptionCriteria() {}

    public OptionCriteria(OptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.isMulti = other.isMulti == null ? null : other.isMulti.copy();
        this.isRequired = other.isRequired == null ? null : other.isRequired.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.optionDetailId = other.optionDetailId == null ? null : other.optionDetailId.copy();
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
    }
}
