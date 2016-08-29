package com.mobilevanity.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Product;
import com.mobilevanity.backend.data.Sale;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class RealInsertSaleServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            String productId = req.getParameter("productid");
            String startDay = req.getParameter("startDay");
            String endDay = req.getParameter("endDay");
            String title = req.getParameter("title");
            String link = req.getParameter("link");
            Product product = null;
            if (!Utility.isEmpty(productId)) {
                long id = Long.parseLong(productId);
                product = DataManager.getInstance().getProduct(id);
            }
            Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            List<BlobKey> blobKeys = blobs.get("banner");
            String imageUrl = null;
            if (blobKeys != null && !blobKeys.isEmpty()) {
                String url = req.getScheme() + "://" + req.getServerName();
                if (!((req.getScheme().toLowerCase().equals("http") && req.getServerPort() == 80) ||
                        (req.getScheme().toLowerCase().equals("https") && req.getServerPort() == 443))) {
                    url += ":" + req.getServerPort();
                }
                imageUrl = url + "/" + "displayimage?imageid=" +blobKeys.get(0).getKeyString();
            }
            if (product != null) {
                try {
                    Sale sale = new Sale();
                    sale.product = Ref.create(product);
                    sale.startDay = Utility.convertStringToDate(startDay);
                    sale.endDay = Utility.convertStringToDate(endDay);
                    sale.title = title;
                    sale.link = link;
                    sale.banner = imageUrl;
                    DataManager.getInstance().saveSale(sale);
                    Utility.responseSuccessMessage(resp, sale.convertResponse());
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
