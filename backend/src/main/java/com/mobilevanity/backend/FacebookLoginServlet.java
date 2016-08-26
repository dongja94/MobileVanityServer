package com.mobilevanity.backend;

import com.mobilevanity.backend.common.FacebookInfo;
import com.mobilevanity.backend.common.FacebookLoginModule;
import com.mobilevanity.backend.common.InvalidUserInfoException;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FacebookLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("access_token");
        String registrationId = req.getParameter("registrationId");
        try {
            FacebookInfo info = FacebookLoginModule.getFacebookInfo(token);
            User user = DataManager.getInstance().getUserByFacebookId(info.id);
            if (user != null) {
                if (user.registrationId == null || !user.registrationId.equals(registrationId)) {
                    user.registrationId = registrationId;
                    DataManager.getInstance().saveUser(user);
                }
                req.getSession().setAttribute(SessionConstant.USER, user);
                User respUser = user.clone();
                respUser.password = null;
                respUser.registrationId = null;
                Utility.responseSuccessMessage(resp, respUser);
            } else {
                req.getSession().setAttribute(SessionConstant.FACEBOOK_INFO, info);
                req.getSession().setAttribute(SessionConstant.REGISTRATION_ID, registrationId);
                Utility.responseMessage(resp, Result.NEED_SIGNUP, info, null);
            }
            return;
        } catch (InvalidUserInfoException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
