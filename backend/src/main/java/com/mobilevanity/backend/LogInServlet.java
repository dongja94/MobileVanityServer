package com.mobilevanity.backend;

import com.google.gson.Gson;
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
public class LogInServlet extends HttpServlet {

    Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String registrationId = req.getParameter("registrationId");
        try {
            User user = DataManager.getInstance().login(email, password);
            if (user.registrationId == null || !user.registrationId.equals(registrationId)) {
                user.registrationId = registrationId;
                DataManager.getInstance().saveUser(user);
            }
            user.password = null;
            user.registrationId = null;
            req.getSession().setAttribute(SessionConstant.USER, user);
            Utility.responseSuccessMessage(resp, user);
            return;
        } catch (InvalidUserInfoException e) {
            e.printStackTrace();
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
