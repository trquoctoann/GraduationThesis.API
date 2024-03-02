package com.cheems.pizzatalk.modules.deal.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.DealStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class DealCriteria extends QuerySelfValidating<DealCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter description;

    private DealStatusFilter status;

    private StringFilter dealNo;

    private RangeFilter<Float> price;

    private StringFilter slug;

    private StringFilter imagePath;

    private RangeFilter<Long> parentDealId;

    private RangeFilter<Long> dealVariationId;

    public DealCriteria() {}

    public DealCriteria(DealCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dealNo = other.dealNo == null ? null : other.dealNo.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.imagePath = other.imagePath == null ? null : other.imagePath.copy();
        this.parentDealId = other.parentDealId == null ? null : other.parentDealId.copy();
        this.dealVariationId = other.dealVariationId == null ? null : other.dealVariationId.copy();
    }

    @Override
    public DealCriteria copy() {
        return new DealCriteria(this);
    }
}
