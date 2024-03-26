package com.cheems.pizzatalk.modules.conversation.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.ConversationTypeFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ConversationCriteria extends QuerySelfValidating<ConversationCriteria> implements Criteria, Serializable {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private ConversationTypeFilter type;

    private RangeFilter<Long> userId;

    private StringFilter username;

    private RangeFilter<Long> chatMessageId;

    public ConversationCriteria() {}

    public ConversationCriteria(ConversationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.chatMessageId = other.chatMessageId == null ? null : other.chatMessageId.copy();
    }

    @Override
    public ConversationCriteria copy() {
        return new ConversationCriteria(this);
    }
}
