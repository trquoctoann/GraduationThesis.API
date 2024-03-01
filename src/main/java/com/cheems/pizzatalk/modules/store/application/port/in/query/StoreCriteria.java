package com.cheems.pizzatalk.modules.store.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.filter.OperationalStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class StoreCriteria extends QuerySelfValidating<StoreCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private StringFilter name;

    private StringFilter address;

    private StringFilter phoneNumber;

    private StringFilter email;

    private OperationalStatusFilter status;

    private Filter<Boolean> allowDelivery;

    private Filter<Boolean> allowPickup;

    private StringFilter country;

    private StringFilter state;

    private StringFilter district;

    private RangeFilter<Double> longitude;

    private RangeFilter<Double> latitude;

    private StringFilter openingHour;

    private StringFilter imagePath;

    private RangeFilter<Long> areaId;

    public StoreCriteria() {}

    public StoreCriteria(StoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.allowDelivery = other.allowDelivery == null ? null : other.allowDelivery.copy();
        this.allowPickup = other.allowPickup == null ? null : other.allowPickup.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.district = other.district == null ? null : other.district.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.openingHour = other.openingHour == null ? null : other.openingHour.copy();
        this.imagePath = other.imagePath == null ? null : other.imagePath.copy();
        this.areaId = other.areaId == null ? null : other.areaId.copy();
    }

    @Override
    public StoreCriteria copy() {
        return new StoreCriteria(this);
    }
}
