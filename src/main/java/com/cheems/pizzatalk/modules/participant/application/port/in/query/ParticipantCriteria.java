package com.cheems.pizzatalk.modules.participant.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.entities.filter.ParticipantStatusFilter;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ParticipantCriteria extends QuerySelfValidating<ParticipantCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private ParticipantStatusFilter status;

    private Filter<Instant> joinedAt;

    private Filter<Instant> leftAt;

    private Filter<Instant> deletedAt;

    private RangeFilter<Long> userId;

    private RangeFilter<Long> conversationId;

    public ParticipantCriteria() {}

    public ParticipantCriteria(ParticipantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.joinedAt = other.joinedAt == null ? null : other.joinedAt.copy();
        this.leftAt = other.leftAt == null ? null : other.leftAt.copy();
        this.deletedAt = other.deletedAt == null ? null : other.deletedAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.conversationId = other.conversationId == null ? null : other.conversationId.copy();
    }

    @Override
    public ParticipantCriteria copy() {
        return new ParticipantCriteria(this);
    }
}
