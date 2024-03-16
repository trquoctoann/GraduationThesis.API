package com.cheems.pizzatalk.modules.product.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.CommerceStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ProductCriteria extends QuerySelfValidating<ProductCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter size;

    private StringFilter slug;

    private StringFilter description;

    private StringFilter sku;

    private CommerceStatusFilter status;

    private StringFilter imagePath;

    private RangeFilter<Long> parentProductId;

    private RangeFilter<Long> categoryId;

    private RangeFilter<Long> productVariationId;

    private RangeFilter<Long> optionId;

    private RangeFilter<Long> storeId;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.size = other.size == null ? null : other.size.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.imagePath = other.imagePath == null ? null : other.imagePath.copy();
        this.parentProductId = other.parentProductId == null ? null : other.parentProductId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.productVariationId = other.productVariationId == null ? null : other.productVariationId.copy();
        this.optionId = other.optionId == null ? null : other.optionId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }
}
