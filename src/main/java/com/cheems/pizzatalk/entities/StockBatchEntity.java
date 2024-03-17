package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.RefillStatus;
import com.cheems.pizzatalk.entities.enumeration.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "stock_batch")
public class StockBatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @NotNull
    @Column(name = "purchase_price_per_unit", nullable = false)
    private Float purchasePricePerUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefillStatus status;

    @Column(name = "remaining_quantity", nullable = false)
    private Long remainingQuantity;

    @NotNull
    @Column(name = "ordered_quantity", nullable = false)
    private Long orderedQuantity;

    @Column(name = "received_quantity", nullable = false)
    private Long receivedQuantity;

    @NotNull
    @Column(name = "ordered_date", nullable = false)
    private Instant orderedDate;

    @Column(name = "received_date", nullable = false)
    private Instant receivedDate;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "store", "product", "optionDetail", "stockBatches" }, allowSetters = true)
    private StockItemEntity stockItem;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockBatchEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public StockBatchEntity unit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public Float getPurchasePricePerUnit() {
        return this.purchasePricePerUnit;
    }

    public void setPurchasePricePerUnit(Float purchasePricePerUnit) {
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    public StockBatchEntity purchasePricePerUnit(Float purchasePricePerUnit) {
        this.purchasePricePerUnit = purchasePricePerUnit;
        return this;
    }

    public RefillStatus getStatus() {
        return this.status;
    }

    public void setStatus(RefillStatus status) {
        this.status = status;
    }

    public StockBatchEntity status(RefillStatus status) {
        this.status = status;
        return this;
    }

    public Long getRemainingQuantity() {
        return this.remainingQuantity;
    }

    public void setRemainingQuantity(Long remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public StockBatchEntity remainingQuantity(Long remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
        return this;
    }

    public Long getOrderedQuantity() {
        return this.orderedQuantity;
    }

    public void setOrderedQuantity(Long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public StockBatchEntity orderedQuantity(Long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
        return this;
    }

    public Long getReceivedQuantity() {
        return this.receivedQuantity;
    }

    public void setReceivedQuantity(Long receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public StockBatchEntity receivedQuantity(Long receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
        return this;
    }

    public Instant getOrderedDate() {
        return this.orderedDate;
    }

    public void setOrderedDate(Instant orderedDate) {
        this.orderedDate = orderedDate;
    }

    public StockBatchEntity orderedDate(Instant orderedDate) {
        this.orderedDate = orderedDate;
        return this;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public StockBatchEntity receivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public StockBatchEntity expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public StockItemEntity getStockItem() {
        return this.stockItem;
    }

    public void setStockItem(StockItemEntity stockItem) {
        this.stockItem = stockItem;
    }

    public StockBatchEntity stockItem(StockItemEntity stockItem) {
        this.stockItem = stockItem;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockBatchEntity)) {
            return false;
        }
        return id != null && id.equals(((StockBatchEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "StockBatch{" +
            "id=" +
            getId() +
            ", unit='" +
            getUnit() +
            "'" +
            ", purchasePricePerUnit='" +
            getPurchasePricePerUnit() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", remainingQuantity='" +
            getRemainingQuantity() +
            "'" +
            ", orderedQuantity='" +
            getOrderedQuantity() +
            "'" +
            ", receivedQuantity='" +
            getReceivedQuantity() +
            "'" +
            ", orderedDate='" +
            getOrderedDate() +
            "'" +
            ", receivedDate='" +
            getReceivedDate() +
            "'" +
            ", expirationDate='" +
            getExpirationDate() +
            "'" +
            "}"
        );
    }
}
