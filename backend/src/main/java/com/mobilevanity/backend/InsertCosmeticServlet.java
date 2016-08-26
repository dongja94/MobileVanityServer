package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Cosmetic;
import com.mobilevanity.backend.data.Product;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertCosmeticServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            long productid = Long.parseLong(req.getParameter("productid"));
            Product product = DataManager.getInstance().getProduct(productid);
            String colorName = req.getParameter("colorName");
            String colorText = req.getParameter("color");
            int color = 0;
            if (!Utility.isEmpty(colorText)) {
                color = Integer.parseInt(req.getParameter("color"), 16);
            }
            float capacity = Float.parseFloat(req.getParameter("capacity"));
            int unit = Integer.parseInt(req.getParameter("unit"));
            String barcode = req.getParameter("barcode");

            String image = null;
            if (!Utility.isEmpty(req.getParameter("image"))) {

                String url = req.getScheme() + "://" + req.getServerName();
                if (!((req.getScheme().toLowerCase().equals("http") && req.getServerPort() == 80) ||
                        (req.getScheme().toLowerCase().equals("https") && req.getServerPort() == 443))) {
                    url += ":" + req.getServerPort();
                }
                image = url + "/images/" + req.getParameter("image");
            }

            Cosmetic cosmetic = new Cosmetic();
            cosmetic.product = Ref.create(product);
            cosmetic.colorName = colorName;
            cosmetic.color = color;
            cosmetic.capacity = capacity;
            cosmetic.unit = unit;
            cosmetic.barcode = barcode;
            cosmetic.image = image;
            DataManager.getInstance().saveCosmetic(cosmetic);
            Utility.responseSuccessMessage(resp, cosmetic.convertResponse());
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
