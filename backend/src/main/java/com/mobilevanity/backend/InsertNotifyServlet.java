package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.Notify;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertNotifyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            String type = req.getParameter("type");
            String beautytipid = req.getParameter("beautytipid");
            String message = req.getParameter("message");
            User notiUser = null;
            BeautyTip beautyTip = null;
            if (!Utility.isEmpty(beautytipid)) {
                long id = Long.parseLong(beautytipid);
                beautyTip = DataManager.getInstance().getBeautyTip(id);
            }
            if (beautyTip != null) {
                Notify notify = new Notify();
                notify.user = beautyTip.user;
                notify.beautyTipId = Ref.create(beautyTip);
                notify.type = type;
                notify.message = message;
                DataManager.getInstance().saveNotify(notify);
                Utility.responseSuccessMessage(resp, notify.convertResponse());
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
