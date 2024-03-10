package com.cheems.pizzatalk.modules.role.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class RoleCriteria extends QuerySelfValidating<RoleCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private RangeFilter<Long> userId;

    public RoleCriteria() {}

    public RoleCriteria(RoleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public RoleCriteria copy() {
        return new RoleCriteria(this);
    }
}
