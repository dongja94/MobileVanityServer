package com.mobilevanity.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.mobilevanity.backend.common.DataConverter;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class FAQ implements DataConverter<FAQ> {
    @Id public Long id;
    public String title;
    public String content;

    @Override
    public FAQ convertResponse() {
        return this;
    }
}