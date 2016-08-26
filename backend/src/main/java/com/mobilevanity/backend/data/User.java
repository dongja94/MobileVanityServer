package com.mobilevanity.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mobilevanity.backend.common.DataConverter;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-08-24.
 */
@Entity
public class User implements Cloneable, Serializable, DataConverter<User.SimpleUserResponse> {
    @Id public Long id;
    public String userNickName;
    public String userProfile;
    public int skinType;
    public int skinTone;
    public int eyeNum;
    public int lipNum;
    public int skinNum;
    public int faceNum;
    public int cleansingNum;
    public int toolNum;
    public int gender;
    @Index public String email;
    @Index public String password;
    @Index public String facebookId;
    public String registrationId;

    public static class SimpleUserResponse {
        public long id;
        public String userNickName;
        public String userProfile;
    }

    @Override
    public SimpleUserResponse convertResponse() {
        SimpleUserResponse suser = new SimpleUserResponse();
        suser.id = id;
        suser.userNickName = userNickName;
        suser.userProfile = userProfile;
        return suser;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        User user = (User)super.clone();
        user.id = id;
        user.userNickName = userNickName;
        user.password = password;
        user.email = email;
        user.registrationId = registrationId;
        user.userProfile = userProfile;
        user.skinType = skinType;
        user.skinTone = skinTone;
        user.eyeNum = eyeNum;
        user.lipNum = lipNum;
        user.skinNum = skinNum;
        user.faceNum = faceNum;
        user.cleansingNum = cleansingNum;
        user.toolNum = toolNum;
        user.gender = gender;
        return user;
    }

}
