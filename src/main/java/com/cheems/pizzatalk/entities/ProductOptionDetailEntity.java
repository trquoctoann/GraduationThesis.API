package com.cheems.pizzatalk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "product_option_detail")
public class ProductOptionDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "product", "option", "productOptionDetails" }, allowSetters = true)
    private ProductOptionEntity productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "option", "productOptionDetails" }, allowSetters = true)
    private OptionDetailEntity optionDetail;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOptionDetailEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ProductOptionEntity getProductOption() {
        return this.productOption;
    }

    public void setProductOption(ProductOptionEntity productOption) {
        this.productOption = productOption;
    }

    public ProductOptionDetailEntity productOption(ProductOptionEntity productOption) {
        this.productOption = productOption;
        return this;
    }

    public OptionDetailEntity getOptionDetail() {
        return this.optionDetail;
    }

    public void setOptionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
    }

    public ProductOptionDetailEntity optionDetail(OptionDetailEntity optionDetail) {
        this.optionDetail = optionDetail;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionDetailEntity)) {
            return false;
        }
        return id != null && id.equals(((ProductOptionDetailEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductOptionDetail{" + "id=" + getId() + "}";
    }
}
