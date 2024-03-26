package com.cheems.pizzatalk.modules.user.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.UserStatusFilter;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UserCriteria extends QuerySelfValidating<UserCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter username;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private UserStatusFilter status;

    private StringFilter langKey;

    private Filter<Boolean> isOnline;

    private Filter<Instant> lastOnlineAt;

    private RangeFilter<Long> roleId;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.isOnline = other.isOnline == null ? null : other.isOnline.copy();
        this.lastOnlineAt = other.lastOnlineAt == null ? null : other.lastOnlineAt.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }
}
