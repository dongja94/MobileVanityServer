package com.mobilevanity.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class FAQ implements DataConverter<FAQ> {
    @Id public Long id;
    @Index public int number;
    public String title;
    public String content;

    @Override
    public FAQ convertResponse(Object... args) {
        return this;
    }
}
