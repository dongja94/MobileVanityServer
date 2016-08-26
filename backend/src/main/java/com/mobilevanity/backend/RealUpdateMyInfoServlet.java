package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
public class RealUpdateMyInfoServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String nickname = req.getParameter("userNickName");
            int skinType = 0;
            try {
                skinType = Integer.parseInt(req.getParameter("skinType"));
            } catch (Exception e) {
            }
            int skinTone = 0;
            try {
                skinTone = Integer.parseInt(req.getParameter("skinTone"));
            } catch (Exception e) {
            }
            int gender = 0;
            try {
                gender = Integer.parseInt(req.getParameter("gender"));
            } catch (Exception e) {
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
            user.userNickName = nickname;
            user.userProfile = imageUrl;
            user.skinTone = skinTone;
            user.skinType = skinType;
            user.gender = gender;
            DataManager.getInstance().saveUser(user);
            req.getSession().setAttribute(SessionConstant.USER,user);
            Utility.responseSuccessMessage(resp, user);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
