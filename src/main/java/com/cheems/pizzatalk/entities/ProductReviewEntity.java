package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "product_review")
public class ProductReviewEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parentProductReview", "childProductReviews", "product" }, allowSetters = true)
    private ProductReviewEntity parentProductReview;

    @OneToMany(mappedBy = "parentProductReview")
    @JsonIgnoreProperties(value = { "parentProductReview", "childProductReviews", "product" }, allowSetters = true)
    private Set<ProductReviewEntity> childProductReviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "parentProduct", "productVariations", "productOptions", "stockItems", "productReviews", "category" },
        allowSetters = true
    )
    private ProductEntity product;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductReviewEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProductReviewEntity content(String content) {
        this.content = content;
        return this;
    }

    public Float getRating() {
        return this.rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public ProductReviewEntity rating(Float rating) {
        this.rating = rating;
        return this;
    }

    public ProductReviewEntity getParentProductReview() {
        return this.parentProductReview;
    }

    public void setParentProductReview(ProductReviewEntity parentProductReview) {
        this.parentProductReview = parentProductReview;
    }

    public ProductReviewEntity parentProductReview(ProductReviewEntity parentProductReview) {
        this.parentProductReview = parentProductReview;
        return this;
    }

    public Set<ProductReviewEntity> getChildProductReviews() {
        return this.childProductReviews;
    }

    public void setChildProductReviews(Set<ProductReviewEntity> childProductReviews) {
        if (this.childProductReviews != null) {
            this.childProductReviews.forEach(childProductReview -> childProductReview.setParentProductReview(null));
        }
        if (childProductReviews != null) {
            childProductReviews.forEach(childProductReview -> childProductReview.setParentProductReview(childProductReview));
        }
        this.childProductReviews = childProductReviews;
    }

    public ProductReviewEntity childProductReviews(Set<ProductReviewEntity> childProductReviews) {
        this.setChildProductReviews(childProductReviews);
        return this;
    }

    public ProductReviewEntity addChildProductReview(ProductReviewEntity childProductReview) {
        childProductReview.setParentProductReview(this);
        this.childProductReviews.add(childProductReview);
        return this;
    }

    public ProductReviewEntity removeChildProductReview(ProductReviewEntity childProductReview) {
        childProductReview.setParentProductReview(null);
        this.childProductReviews.remove(childProductReview);
        return this;
    }

    public ProductEntity getProduct() {
        return this.product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ProductReviewEntity product(ProductEntity product) {
        this.product = product;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReviewEntity)) {
            return false;
        }
        return id != null && id.equals(((ProductReviewEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("ProductReview{" + "id=" + getId() + ", content='" + getContent() + "'" + ", rating='" + getRating() + "'" + "}");
    }
}
