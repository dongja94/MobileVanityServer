package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.Comment;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class CommentListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String beautyTipId = req.getParameter("beautytipid");
        BeautyTip beautyTip = null;
        if (!Utility.isEmpty(beautyTipId)) {
            long id = Long.parseLong(beautyTipId);
            beautyTip = DataManager.getInstance().getBeautyTip(id);
        }
        if (beautyTip != null) {
            List<Comment> comments = DataManager.getInstance().findComment(beautyTip);
            for (Comment comment : comments) {
                comment.beautyTip = null;
            }
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(comments));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
