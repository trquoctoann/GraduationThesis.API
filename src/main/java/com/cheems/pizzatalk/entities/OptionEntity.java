package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "`option`")
public class OptionEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommerceStatus status;

    @NotNull
    @Column(name = "is_multi", nullable = false)
    private Boolean isMulti;

    @NotNull
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @OneToMany(mappedBy = "option")
    @JsonIgnoreProperties(value = { "product", "option", "productOptionDetails" }, allowSetters = true)
    private Set<ProductOptionEntity> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "option")
    @JsonIgnoreProperties(value = { "option", "productOptionDetails", "stockItems" }, allowSetters = true)
    private Set<OptionDetailEntity> optionDetails = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OptionEntity code(String code) {
        this.code = code;
        return this;
    }

    public CommerceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CommerceStatus status) {
        this.status = status;
    }

    public OptionEntity status(CommerceStatus status) {
        this.status = status;
        return this;
    }

    public Boolean getIsMulti() {
        return this.isMulti;
    }

    public void setIsMulti(Boolean isMulti) {
        this.isMulti = isMulti;
    }

    public OptionEntity isMulti(Boolean isMulti) {
        this.isMulti = isMulti;
        return this;
    }

    public Boolean getIsRequired() {
        return this.isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public OptionEntity isRequired(Boolean isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    public Set<ProductOptionEntity> getProductOptions() {
        return this.productOptions;
    }

    public void setProductOptions(Set<ProductOptionEntity> productOptions) {
        if (this.productOptions != null) {
            this.productOptions.forEach(productOption -> productOption.setOption(null));
        }
        if (productOptions != null) {
            productOptions.forEach(productOption -> productOption.setOption(this));
        }
        this.productOptions = productOptions;
    }

    public OptionEntity productOptions(Set<ProductOptionEntity> productOptions) {
        this.productOptions = productOptions;
        return this;
    }

    public OptionEntity addProductOption(ProductOptionEntity productOption) {
        productOption.setOption(this);
        this.productOptions.add(productOption);
        return this;
    }

    public OptionEntity removeProductOption(ProductOptionEntity productOption) {
        productOption.setOption(null);
        this.productOptions.remove(productOption);
        return this;
    }

    public Set<OptionDetailEntity> getOptionDetails() {
        return this.optionDetails;
    }

    public void setOptionDetails(Set<OptionDetailEntity> optionDetails) {
        if (this.optionDetails != null) {
            this.optionDetails.forEach(optionDetail -> optionDetail.setOption(null));
        }
        if (optionDetails != null) {
            optionDetails.forEach(optionDetail -> optionDetail.setOption(this));
        }
        this.optionDetails = optionDetails;
    }

    public OptionEntity optionDetails(Set<OptionDetailEntity> optionDetails) {
        this.setOptionDetails(optionDetails);
        return this;
    }

    public OptionEntity addOptionDetail(OptionDetailEntity optionDetail) {
        optionDetail.setOption(this);
        this.optionDetails.add(optionDetail);
        return this;
    }

    public OptionEntity removeOptionDetail(OptionDetailEntity optionDetail) {
        optionDetail.setOption(null);
        this.optionDetails.remove(optionDetail);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionEntity)) {
            return false;
        }
        return id != null && id.equals(((OptionEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Option={" +
            "id=" +
            getId() +
            ", code='" +
            getCode() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", isMulti='" +
            getIsMulti() +
            "'" +
            ", isRequired='" +
            getIsRequired() +
            "'" +
            "}"
        );
    }
}
