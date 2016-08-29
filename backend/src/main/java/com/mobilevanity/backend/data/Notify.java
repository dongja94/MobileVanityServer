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
public class Notify implements DataConverter<Notify.NotifyResponse> {
    @Id public Long id;
    @Index public Ref<User> user;
    @Index public String type;
    public Ref<BeautyTip> beautyTipId;
    public String message;
    @Index public Date date = new Date();

    public static class NotifyResponse {
        public long id;
        public String type;
        public Ref<BeautyTip> beautyTipId;
        public String message;
        public String date;
    }

    @Override
    public NotifyResponse convertResponse(Object... args) {
        NotifyResponse nr = new NotifyResponse();
        nr.id = id;
        nr.type = type;
        nr.beautyTipId = beautyTipId;
        nr.message = message;
        nr.date = Utility.convertDateToString(date);
        return nr;
    }
}
