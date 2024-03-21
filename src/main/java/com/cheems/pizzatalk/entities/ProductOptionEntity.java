package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parentProduct", "productVariations", "productOptions", "stockItems", "category" }, allowSetters = true)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productOptions", "optionDetails" }, allowSetters = true)
    private OptionEntity option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "option", "productOptions", "stockItems" }, allowSetters = true)
    private OptionDetailEntity optionDetail;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOptionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ProductEntity getProduct() {
        return this.product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ProductOptionEntity product(ProductEntity product) {
        this.product = product;
        return this;
    }

    public OptionEntity getOption() {
        return this.option;
    }

    public void setOption(OptionEntity option) {
        this.option = option;
    }

    public ProductOptionEntity option(OptionEntity option) {
        this.option = option;
        return this;
    }

    public OptionDetailEntity getOptionDetail() {
        return this.optionDetail;
    }

    public void setOptionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
    }

    public ProductOptionEntity optionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionEntity)) {
            return false;
        }
        return id != null && id.equals(((ProductOptionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductOption{" + "id=" + getId() + "}";
    }
}
