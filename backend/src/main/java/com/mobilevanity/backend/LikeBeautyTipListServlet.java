package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class LikeBeautyTipListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            List<BeautyTip> beautyTips = DataManager.getInstance().findLikeBeautyTip(user);
            for (BeautyTip beautyTip : beautyTips) {
                beautyTip.user = null;
            }
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(beautyTips));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
