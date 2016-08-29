package com.mobilevanity.backend.common;

/**
 * Created by Administrator on 2016-08-25.
 */
public interface DataConverter<DR> {
    public DR convertResponse(Object... args);
}
