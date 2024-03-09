package com.cheems.pizzatalk.modules.permission.application.port.in.query;

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
@ToString(callSuper = true)
public class PermissionCriteria extends QuerySelfValidating<PermissionCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private RangeFilter<Long> roleId;

    public PermissionCriteria() {}

    public PermissionCriteria(PermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
    }

    @Override
    public PermissionCriteria copy() {
        return new PermissionCriteria(this);
    }
}
