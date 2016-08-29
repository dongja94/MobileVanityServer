package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.util.ArrayList;
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
//            List<BeautyTip> beautyTips = DataManager.getInstance().findLikeBeautyTip(user);
//            for (BeautyTip beautyTip : beautyTips) {
//                beautyTip.user = null;
//            }
            List<BeautyTip> all = DataManager.getInstance().listBeautyTip();
            List<BeautyTip> beautyTips = new ArrayList<>();
            Ref<User> ref = Ref.create(user);
            for (BeautyTip bt : all) {
                if (bt.likeUsers.contains(ref)) {
                    bt.user = null;
                    beautyTips.add(bt);
                }
            }
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(beautyTips, user));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
