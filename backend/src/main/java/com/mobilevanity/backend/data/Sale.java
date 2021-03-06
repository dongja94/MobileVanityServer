package com.mobilevanity.backend.data;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;
import com.mobilevanity.backend.common.Utility;

import java.util.Date;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class Sale implements DataConverter<Sale.SaleResponse> {
    @Id public Long id;
    @Index public Ref<Product> product;
    @Index public Date startDay;
    @Index public Date endDay;
    public String title;
    public String link;
    public String banner;

    public static class SaleResponse {
        public long id;
        public Product.ProductResponse product;
        public String startDay;
        public String endDay;
        public String title;
        public String link;
        public String banner;
    }

    @Override
    public SaleResponse convertResponse(Object... args) {
        SaleResponse sr = new SaleResponse();
        sr.id = id;
        if (product != null) {
            sr.product = product.get().convertResponse();
        }
        sr.title = title;
        sr.link = link;
        sr.banner = banner;
        sr.startDay = Utility.convertDateToString(startDay);
        sr.endDay = Utility.convertDateToString(endDay);
        return sr;
    }
}
