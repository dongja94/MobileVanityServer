package com.mobilevanity.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class Brand implements DataConverter<Brand> {
    @Id public Long id;
    @Index public String name;

    @Override
    public Brand convertResponse() {
        return this;
    }
}
