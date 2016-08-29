package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.Comment;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class DeleteBeautyTipServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String beautyTipId = req.getParameter("beautytipid");
            BeautyTip beautyTip = null;
            if (!Utility.isEmpty(beautyTipId)) {
                long id = Long.parseLong(beautyTipId);
                beautyTip = DataManager.getInstance().getBeautyTip(id);
            }
            if (beautyTip != null && beautyTip.user.get().id.equals(user.id)) {
                List<Comment> comments = DataManager.getInstance().findComment(beautyTip);
                DataManager.getInstance().deleteCommentAll(comments);
                DataManager.getInstance().deleteBeautyTip(beautyTip);
                Utility.responseSuccessMessage(resp, beautyTip.convertResponse(user));
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
