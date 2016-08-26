package com.mobilevanity.backend.data;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;
import com.mobilevanity.backend.common.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class BeautyTip implements DataConverter<BeautyTip.BeautyTipResponse> {
    public static final int SORT_TYPE_RECENT = 1;
    public static final int SORT_TYPE_LIKECOUNT = 2;

    @Id public Long id;
    public String previewImage;
    @Index public String title;
    @Index public String content;
    @Index public Ref<User> user;
    @Index public List<Ref<User>> likeUsers = new ArrayList<>();
    @Index public int likeCount;
    public int commentNum;
    public int readNum;
    @Index public Date writeDate = new Date();

    public static class BeautyTipResponse {
        public long id;
        public String previewImage;
        public String title;
        public String content;
        public User.SimpleUserResponse user;
        public int likeCount;
        public int commentNum;
        public int readNum;
        public String writeDate;
    }

    @Override
    public BeautyTipResponse convertResponse() {
        BeautyTipResponse btr = new BeautyTipResponse();
        btr.id = id;
        btr.previewImage = previewImage;
        btr.title = title;
        btr.content = content;
        if (user != null) {
            btr.user = user.get().convertResponse();
        }
        btr.likeCount = likeCount;
        btr.commentNum = commentNum;
        btr.readNum = readNum;
        btr.writeDate = Utility.convertDateToString(writeDate);
        return btr;
    }
}
