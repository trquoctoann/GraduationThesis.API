package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.AttachmentType;

public class AttachmentTypeFilter extends Filter<AttachmentType> {

    public AttachmentTypeFilter() {}

    public AttachmentTypeFilter(AttachmentTypeFilter filter) {
        super(filter);
    }

    @Override
    public AttachmentTypeFilter copy() {
        return new AttachmentTypeFilter(this);
    }
}
