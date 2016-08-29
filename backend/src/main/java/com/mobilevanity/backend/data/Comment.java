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
public class Comment implements DataConverter<Comment.CommentResponse> {
    @Id public Long id;
    @Index public Ref<User> writer;
    @Index public Ref<BeautyTip> beautyTip;
    public String content;
    @Index public Date writeDate = new Date();

    public static class CommentResponse {
        public long id;
        public User.SimpleUserResponse writer;
        public BeautyTip.BeautyTipResponse beautyTip;
        public String content;
        public String writeDate;
    }

    @Override
    public CommentResponse convertResponse(Object... args) {
        CommentResponse cr = new CommentResponse();
        cr.id = id;
        if (writer != null) {
            cr.writer = writer.get().convertResponse();
        }
        if (beautyTip != null) {
            cr.beautyTip = beautyTip.get().convertResponse();
        }
        cr.content = content;
        cr.writeDate = Utility.convertDateToString(writeDate);
        return cr;
    }
}
