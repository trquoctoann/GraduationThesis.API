package com.cheems.pizzatalk.modules.conversation.domain;

import java.util.Map;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConversationMetadata {

    private String title;

    private String avatar;

    private Map<String, String> memberNames;

    private Integer countMembers;
}
