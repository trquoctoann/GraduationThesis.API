package com.cheems.pizzatalk.common.specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

    private static final String SEPARATE_CHARACTER = "\\.";

    private SpecificationUtils() {}

    public static <T> Specification<T> distinct(boolean isDistinct) {
        return (root, query, builder) -> {
            query.distinct(isDistinct);
            return builder.and();
        };
    }

    public static <T> Specification<T> sortBy(String sortType, String... columnNames) {
        return (root, query, builder) -> {
            List<Order> orderList = new ArrayList<>();
            if (sortType.equalsIgnoreCase(sortType)) {
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
