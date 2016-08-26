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
public class CosmeticItem implements DataConverter<CosmeticItem.CosmeticItemResponse> {
    @Id public Long id;
    @Index public Ref<Cosmetic> cosmetic;
    @Index public Date dateAdded;
    public int cosmeticTerm;
    @Index public Ref<User> owner;

    public static class CosmeticItemResponse {
        public long id;
        public Cosmetic.CosmeticResponse cosmetic;
        public String dateAdded;
        public int cosmeticTerm;
        public User.SimpleUserResponse owner;
    }

    @Override
    public CosmeticItemResponse convertResponse() {
        CosmeticItemResponse cir = new CosmeticItemResponse();
        cir.id = id;
        if (cosmetic != null) {
            cir.cosmetic = cosmetic.get().convertResponse();
        }
        cir.dateAdded = Utility.convertDateToString(dateAdded);
        cir.cosmeticTerm = cosmeticTerm;
        if (owner != null) {
            cir.owner = owner.get().convertResponse();
        }
        return cir;
    }
}
