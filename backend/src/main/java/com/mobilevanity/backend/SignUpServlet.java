package com.mobilevanity.backend;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.UserAddException;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-10.
 */
public class SignUpServlet extends HttpServlet {
    Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String regId = req.getParameter("registrationId");
        User user = new User();
        user.userNickName = nickname;
        user.password = password;
        user.email = email;
        user.registrationId = regId;
        try {
            User insertUser = DataManager.getInstance().addUser(user);
            req.getSession().setAttribute(SessionConstant.USER, insertUser);
            User u = insertUser.clone();
            u.password = null;
            u.registrationId = null;
            Utility.responseSuccessMessage(resp, u);
            return;
        } catch (UserAddException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
