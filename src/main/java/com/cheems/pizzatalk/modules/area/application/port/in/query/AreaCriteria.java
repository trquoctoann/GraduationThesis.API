package com.cheems.pizzatalk.modules.area.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.OperationalStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class AreaCriteria extends QuerySelfValidating<AreaCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private RangeFilter<Long> originalId;

    private StringFilter name;

    private StringFilter code;

    private StringFilter brandCode;

    private OperationalStatusFilter status;

    private RangeFilter<Long> storeCount;

    private StringFilter priceGroupId;

    private RangeFilter<Long> storeId;

    public AreaCriteria() {}

    public AreaCriteria(AreaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.originalId = other.originalId == null ? null : other.originalId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.brandCode = other.brandCode == null ? null : other.brandCode.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.storeCount = other.storeCount == null ? null : other.storeCount.copy();
        this.priceGroupId = other.priceGroupId == null ? null : other.priceGroupId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
    }

    @Override
    public AreaCriteria copy() {
        return new AreaCriteria(this);
    }
}
