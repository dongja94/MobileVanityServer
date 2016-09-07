package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Brand;
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
public class InsertProductServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            long brandid = Long.parseLong(req.getParameter("brandid"));
            Brand brand = DataManager.getInstance().getBrand(brandid);
            int category = Integer.parseInt(req.getParameter("category"));
            int item = Integer.parseInt(req.getParameter("item"));
            String code = req.getParameter("code");
            String name = req.getParameter("name");

            String image = null;
            if (!Utility.isEmpty(req.getParameter("image"))) {

                String url = req.getScheme() + "://" + req.getServerName();
                if (!((req.getScheme().toLowerCase().equals("http") && req.getServerPort() == 80) ||
                        (req.getScheme().toLowerCase().equals("https") && req.getServerPort() == 443))) {
                    url += ":" + req.getServerPort();
                }
                image = url + "/"+brand.images+"/" + req.getParameter("image");
            }

            int useBy = Integer.parseInt(req.getParameter("useBy"));
            Product product = new Product();
            product.brand = Ref.create(brand);
            product.category = category;
            product.item = item;
            product.code = code;
            product.name = name;
            product.image = image;
            product.useBy = useBy;
            DataManager.getInstance().saveProduct(product);
            Utility.responseSuccessMessage(resp, product.convertResponse());
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
