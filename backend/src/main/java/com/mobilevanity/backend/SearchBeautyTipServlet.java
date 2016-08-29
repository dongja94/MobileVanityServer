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
public class SearchBeautyTipServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String orderText = req.getParameter("order");
        int order = BeautyTip.SORT_TYPE_RECENT;
        if (!Utility.isEmpty(orderText)) {
            if (orderText.equals("recent")) {
                order = BeautyTip.SORT_TYPE_RECENT;
            } else if (orderText.equals("rank")) {
                order = BeautyTip.SORT_TYPE_LIKECOUNT;
            }
        }
        if (Utility.isEmpty(type)) {
            type = "user";
            if (!Utility.isEmpty(query)) {
                type = "keyword";
            }
        }
        if (type.equals("user")) {
            User user = (User)req.getSession().getAttribute(SessionConstant.USER);
            if (user != null) {
                List<BeautyTip> beautyTips = DataManager.getInstance().findBeautyTip(user, order, query);
                for (BeautyTip bt : beautyTips) {
                    bt.user = null;
                }
                Utility.responseSuccessMessage(resp, Utility.convertResponseList(beautyTips,user));
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
            return;
        } else if (type.equals("keyword")) {
            if (!Utility.isEmpty(query)) {
                List<BeautyTip> beautyTips = DataManager.getInstance().findBeautyTip(null, order, query);
                User user = (User)req.getSession().getAttribute(SessionConstant.USER);
                Utility.responseSuccessMessage(resp, Utility.convertResponseList(beautyTips,user));
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
