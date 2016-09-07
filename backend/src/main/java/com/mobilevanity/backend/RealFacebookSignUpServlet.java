package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.mobilevanity.backend.common.FacebookInfo;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class RealFacebookSignUpServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FacebookInfo info = (FacebookInfo)req.getSession().getAttribute(SessionConstant.FACEBOOK_INFO);
        if (info != null) {
            User user = new User();
            user.userNickName = req.getParameter("nickname");
            user.email = req.getParameter("email");
            String temp = req.getParameter("skinType");
            if (!Utility.isEmpty(temp)) {
                user.skinType = Integer.parseInt(temp);
            }
            temp = req.getParameter("skinTone");
            if (!Utility.isEmpty(temp)) {
                user.skinTone = Integer.parseInt(temp);
            }
            temp = req.getParameter("gender");
            if (!Utility.isEmpty(temp)) {
                user.gender = Integer.parseInt(temp);
            }
            Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            List<BlobKey> blobKeys = blobs.get("userProfile");
            String imageUrl = null;
            if (blobKeys != null && !blobKeys.isEmpty()) {
                String url = req.getScheme() + "://" + req.getServerName();
                if (!((req.getScheme().toLowerCase().equals("http") && req.getServerPort() == 80) ||
                        (req.getScheme().toLowerCase().equals("https") && req.getServerPort() == 443))) {
                    url += ":" + req.getServerPort();
                }
                imageUrl = url + "/" + "displayimage?imageid=" +blobKeys.get(0).getKeyString();
            }
            user.userProfile = imageUrl;
            user.registrationId = (String)req.getSession().getAttribute(SessionConstant.REGISTRATION_ID);
            user.facebookId = info.id;
            try {
                user = DataManager.getInstance().saveUser(user);
                req.getSession().removeAttribute(SessionConstant.FACEBOOK_INFO);
                req.getSession().removeAttribute(SessionConstant.REGISTRATION_ID);
                req.getSession().setAttribute(SessionConstant.USER, user);
                User respUser = user.clone();
                respUser.registrationId = null;
                Utility.responseSuccessMessage(resp, respUser);
                return;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INFOS[Result.ERROR_NOT_LOGIN].code, "facebook not login");
    }
}
