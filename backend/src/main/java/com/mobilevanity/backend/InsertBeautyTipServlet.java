package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class InsertBeautyTipServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = blobstoreService.createUploadUrl("/realinsertbeautytip");
        URL u = new URL(url);
        req.getRequestDispatcher(u.getFile()).forward(req, resp);
    }
}
