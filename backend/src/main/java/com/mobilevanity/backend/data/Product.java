package com.mobilevanity.backend.data;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class Product implements DataConverter<Product.ProductResponse> {
    @Id public Long id;
    @Index public Ref<Brand> brand;
    @Index public int category;
    @Index public int item;
    public String code;
    public String name;
    public String image;
    public int useBy;

    public static class ProductResponse {
        public long id;
        public Brand brand;
        public int category;
        public int item;
        public String code;
        public String name;
        public String image;
        public int useBy;
    }

    @Override
    public ProductResponse convertResponse() {
        ProductResponse pr = new ProductResponse();
        pr.id = id;
        if (brand != null) {
            pr.brand = brand.get();
        }
        pr.category = category;
        pr.item = item;
        pr.code = code;
        pr.name = name;
        pr.image = image;
        pr.useBy = useBy;
        return pr;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product)) {
            return false;
        }
        Product p = (Product)obj;
        return p.id.equals(id);
    }
}
