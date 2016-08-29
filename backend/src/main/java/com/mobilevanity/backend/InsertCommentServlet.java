package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.Comment;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertCommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String beautytipid = req.getParameter("beautytipid");
            String content = req.getParameter("comment");
            BeautyTip beautyTip = null;
            if (!Utility.isEmpty(beautytipid)) {
                long id = Long.parseLong(beautytipid);
                beautyTip = DataManager.getInstance().getBeautyTip(id);
            }
            if (beautyTip != null) {
                Comment comment = new Comment();
                comment.beautyTip = Ref.create(beautyTip);
                comment.writer = Ref.create(user);
                comment.content = content;
                beautyTip.commentNum++;
                DataManager.getInstance().saveBeautyTip(beautyTip);
                DataManager.getInstance().saveComment(comment);
                Utility.responseSuccessMessage(resp, comment.convertResponse());
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
