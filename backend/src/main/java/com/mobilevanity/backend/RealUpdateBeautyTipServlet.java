package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
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
public class RealUpdateBeautyTipServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String beautyTipId = req.getParameter("beautytipid");
            BeautyTip beautyTip = null;
            if (!Utility.isEmpty(beautyTipId)) {
                long id = Long.parseLong(beautyTipId);
                beautyTip = DataManager.getInstance().getBeautyTip(id);
            }
            if (beautyTip != null && beautyTip.user.get().id == user.id) {
                String title = req.getParameter("title");
                String content = req.getParameter("content");
                Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
                List<BlobKey> blobKeys = blobs.get("image");
                String imageUrl = null;
                if (blobKeys != null && !blobKeys.isEmpty()) {
                    String url = req.getScheme() + "://" + req.getServerName();
                    if (!((req.getScheme().toLowerCase().equals("http") && req.getServerPort() == 80) ||
                            (req.getScheme().toLowerCase().equals("https") && req.getServerPort() == 443))) {
                        url += ":" + req.getServerPort();
                    }
                    imageUrl = url + "/" + "displayimage?imageid=" + blobKeys.get(0).getKeyString();
                }
                if (!Utility.isEmpty(title) && !Utility.isEmpty(content)) {
                    beautyTip.title = title;
                    beautyTip.content = content;
                    if (!Utility.isEmpty(imageUrl)) {
                        beautyTip.previewImage = imageUrl;
                    }
                    DataManager.getInstance().saveBeautyTip(beautyTip);
                    Utility.responseSuccessMessage(resp, beautyTip.convertResponse());
                    return;
                }
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
