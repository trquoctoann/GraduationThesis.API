package com.cheems.pizzatalk.modules.product.domain;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Product implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 2)
    private String size;

    @NotNull
    @Size(max = 50)
    private String slug;

    private String description;

    @NotNull
    @Size(min = 6, max = 6)
    private String sku;

    @NotNull
    private CommerceStatus status;

    @Size(max = 300)
    private String imagePath;

    private Long parentProductId;

    private Product parentProduct;

    private Long categoryId;

    private Category category;

    private Set<Product> productVariations;

    private Set<Option> options;

    private Set<OptionDetail> optionDetails;

    private Set<StockItem> stockItems;
}
