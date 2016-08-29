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
public class Cosmetic implements DataConverter<Cosmetic.CosmeticResponse> {
    @Id public Long id;
    public String image;
    @Index public String colorName;
    public int color;
    public float capacity;
    public int unit;
    @Index public String barcode;
    @Index public Ref<Product> product;

    public static class CosmeticResponse {
        public long id;
        public String image;
        public String colorName;
        public int color;
        public float capacity;
        public int unit;
        public String barcode;
        public Product.ProductResponse product;
    }

    @Override
    public CosmeticResponse convertResponse(Object... args) {
        CosmeticResponse cr = new CosmeticResponse();
        cr.id = id;
        cr.image = image;
        cr.colorName = colorName;
        cr.color = color;
        cr.capacity = capacity;
        cr.unit = unit;
        cr.barcode = barcode;
        if (product != null) {
            cr.product = product.get().convertResponse();
        }
        return cr;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cosmetic)) return false;
        Cosmetic c = (Cosmetic)obj;
        return c.id.equals(id);
    }
}
