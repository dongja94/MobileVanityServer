package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.GcmSender;
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
public class UpdateLikeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String beautyTipId = req.getParameter("beautytipid");
            String likeText = req.getParameter("like");
            boolean like = true;
            BeautyTip beautyTip = null;
            if (!Utility.isEmpty(beautyTipId)) {
                long id = Long.parseLong(beautyTipId);
                beautyTip = DataManager.getInstance().getBeautyTip(id);
            }
            if (!Utility.isEmpty(likeText)) {
                like = Boolean.parseBoolean(likeText);
            }
            if (beautyTip != null) {
                Ref<User> ref = Ref.create(user);
                if (like) {
                    if (!beautyTip.likeUsers.contains(ref)) {
                        beautyTip.likeUsers.add(ref);
                        DataManager.getInstance().saveBeautyTip(beautyTip);

                        User writer = beautyTip.user.get();
                        Notify notify = new Notify();
                        notify.beautyTipId = Ref.create(beautyTip);
                        notify.user = beautyTip.user;
                        notify.type = Notify.TYPE_LIKE;
                        notify.message = String.format(Notify.MESSAGE_FORMAT_LIKE, user.userNickName, beautyTip.title);
                        DataManager.getInstance().saveNotify(notify);
                        GcmSender.getInstance().sendGCM(writer.registrationId);

                        Utility.responseSuccessMessage(resp, beautyTip.convertResponse(user));
                        return;
                    }
                } else {
                    if (beautyTip.likeUsers.contains(ref)) {
                        beautyTip.likeUsers.remove(ref);
                        DataManager.getInstance().saveBeautyTip(beautyTip);
                        Utility.responseSuccessMessage(resp, beautyTip.convertResponse(user));
                        return;
                    }
                }
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
