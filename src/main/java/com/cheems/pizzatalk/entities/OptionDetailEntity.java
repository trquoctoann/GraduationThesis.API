package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "option_detail")
public class OptionDetailEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "value", length = 100, nullable = false)
    private String value;

    @Size(min = 6, max = 6)
    @Column(name = "sku", unique = true, length = 6)
    private String sku;

    @Size(max = 20)
    @Column(name = "code", length = 20)
    private String code;

    @Size(max = 20)
    @Column(name = "uom_id", length = 20)
    private String uomId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommerceStatus status;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productOptions", "optionDetails" }, allowSetters = true)
    private OptionEntity option;

    @OneToMany(mappedBy = "optionDetail")
    @JsonIgnoreProperties(value = { "productOption", "optionDetail" }, allowSetters = true)
    private Set<ProductOptionDetailEntity> productOptionDetails = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionDetailEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OptionDetailEntity value(String value) {
        this.value = value;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public OptionDetailEntity sku(String sku) {
        this.sku = sku;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OptionDetailEntity code(String code) {
        this.code = code;
        return this;
    }

    public String getUomId() {
        return this.uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public OptionDetailEntity uomId(String uomId) {
        this.uomId = uomId;
        return this;
    }

    public CommerceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CommerceStatus status) {
        this.status = status;
    }

    public OptionDetailEntity status(CommerceStatus status) {
        this.status = status;
        return this;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public OptionDetailEntity price(Float price) {
        this.price = price;
        return this;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OptionDetailEntity quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OptionEntity getOption() {
        return this.option;
    }

    public void setOption(OptionEntity option) {
        this.option = option;
    }

    public OptionDetailEntity option(OptionEntity option) {
        this.option = option;
        return this;
    }

    public Set<ProductOptionDetailEntity> getProductOptionDetails() {
        return this.productOptionDetails;
    }

    public void setProductOptionDetails(Set<ProductOptionDetailEntity> productOptionDetails) {
        if (this.productOptionDetails != null) {
            this.productOptionDetails.forEach(productOptionDetail -> productOptionDetail.setOptionDetail(null));
        }
        if (productOptionDetails != null) {
            productOptionDetails.forEach(productOptionDetail -> productOptionDetail.setOptionDetail(this));
        }
        this.productOptionDetails = productOptionDetails;
    }

    public OptionDetailEntity productOptionDetails(Set<ProductOptionDetailEntity> productOptionDetails) {
        this.setProductOptionDetails(productOptionDetails);
        return this;
    }

    public OptionDetailEntity addProductOptionDetail(ProductOptionDetailEntity productOptionDetail) {
        productOptionDetail.setOptionDetail(this);
        return this;
    }

    public OptionDetailEntity removeProductOptionDetail(ProductOptionDetailEntity productOptionDetail) {
        productOptionDetail.setOptionDetail(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDetailEntity)) {
            return false;
        }
        return id != null && id.equals(((OptionDetailEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "OptionDetail={" +
            "id=" +
            getId() +
            ", value='" +
            getValue() +
            "'" +
            ", sku='" +
            getSku() +
            "'" +
            ", code='" +
            getCode() +
            "'" +
            ", uomId='" +
            getUomId() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", price='" +
            getPrice() +
            "'" +
            ", quantity='" +
            getQuantity() +
            "'" +
            "}"
        );
    }
}
