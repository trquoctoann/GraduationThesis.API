package com.cheems.pizzatalk.common.cqrs;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Query {

    private Set<String> fetchAttributes = new HashSet<>();

    public void addFetchAttribute(String attribute) {
        fetchAttributes.add(attribute);
    }
}
