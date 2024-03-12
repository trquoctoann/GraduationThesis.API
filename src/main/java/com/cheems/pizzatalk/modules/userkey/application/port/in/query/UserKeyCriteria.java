package com.cheems.pizzatalk.modules.userkey.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.UserKeyStatusFilter;
import com.cheems.pizzatalk.entities.filter.UserKeyTypeFilter;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UserKeyCriteria extends QuerySelfValidating<UserKeyCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter value;

    private UserKeyTypeFilter type;

    private UserKeyStatusFilter status;

    private Filter<Instant> creationDate;

    private Filter<Instant> usedDate;

    private Filter<Instant> expirationDate;

    private RangeFilter<Long> userId;

    public UserKeyCriteria() {}

    public UserKeyCriteria(UserKeyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.usedDate = other.usedDate == null ? null : other.usedDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserKeyCriteria copy() {
        return new UserKeyCriteria(this);
    }
}
