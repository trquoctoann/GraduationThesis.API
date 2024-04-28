package com.cheems.pizzatalk.common.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

public class SpecificationUtils {

    private static final String SEPARATE_CHARACTER = "\\.";

    private SpecificationUtils() {}

    @SuppressWarnings("unchecked")
    public static <T, Y> Join<T, Y> getExistsJoinFetch(From<?, ?> root, String attributeName) {
        for (Fetch<?, ?> fetch : root.getFetches()) {
            Join<?, ?> join = (Join<?, ?>) fetch;
            if (join.getAttribute().getName().equals(attributeName)) {
                return (Join<T, Y>) join;
            }
        }

        for (Join<?, ?> join : root.getJoins()) {
            if (join.getAttribute().getName().equals(attributeName)) {
                return (Join<T, Y>) join;
            }
        }

        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, Y> Join<T, Y> getJoinFetch(From<?, ?> root, String attributeName, JoinType joinType, boolean isJoin) {
        if (joinType == null) {
            joinType = JoinType.LEFT;
        }

        Join<T, Y> join = getExistsJoinFetch(root, attributeName);
        if (join != null) {
            Optional<?> rootFetchOptional = root
                .getFetches()
                .stream()
                .filter(fetch -> fetch.getAttribute().getName().equals(join.getAttribute().getName()))
                .findFirst();
            if (!isJoin && rootFetchOptional.isEmpty()) {
                root.getJoins().remove(join);
                if (CollectionUtils.isEmpty(root.getFetches())) {
                    Fetch<?, ?> fetch = root.fetch(attributeName, joinType);
                    root.getFetches().remove(fetch);
                }
                root.getFetches().add((Fetch) join);
            }
            return join;
        }

        if (isJoin) {
            return root.join(attributeName, joinType);
        }

        return (Join<T, Y>) root.fetch(attributeName, joinType);
    }

    public static <T> Specification<T> fetchAttributes(Collection<String> attributes) {
        return fetchAttributes(attributes.toArray(new String[0]));
    }

    public static <T> Specification<T> fetchAttributes(String... attributes) {
        return (root, query, builder) -> {
            Class<?> clazz = query.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class) || attributes.length == 0) {
                return builder.and();
            }

            for (String attribute : attributes) {
                String[] attributeNames = attribute.split(SEPARATE_CHARACTER);
                Join<?, ?> join = getJoinFetch(root, attributeNames[0], JoinType.LEFT, false);

                if (attributeNames.length == 1) {
                    continue;
                }

                for (int i = 1; i < attributeNames.length; i++) {
                    join = getJoinFetch(join, attributeNames[i], JoinType.LEFT, false);
                }
            }
            return builder.and();
        };
    }

    public static <T> Specification<T> distinct(boolean isDistinct) {
        return (root, query, builder) -> {
            query.distinct(isDistinct);
            return builder.and();
        };
    }

    public static <T> Specification<T> sortBy(String sortType, String... columnNames) {
        return (root, query, builder) -> {
            List<Order> orderList = new ArrayList<>();
            if (sortType.equalsIgnoreCase("ASC")) {
                for (String columnName : columnNames) {
                    orderList.add(builder.asc(root.get(columnName)));
                }
            } else {
                for (String columnName : columnNames) {
                    orderList.add(builder.desc(root.get(columnName)));
                }
            }
            query.orderBy(orderList);
            return builder.and();
        };
    }
}
