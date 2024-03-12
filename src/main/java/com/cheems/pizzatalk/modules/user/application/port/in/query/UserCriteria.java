package com.cheems.pizzatalk.modules.user.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.UserStatusFilter;
import java.io.Serializable;
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
        this.roleId = other.roleId == null ? null : other.roleId.copy();
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }
}
