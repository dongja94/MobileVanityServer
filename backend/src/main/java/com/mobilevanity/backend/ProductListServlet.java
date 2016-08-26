package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Brand;
import com.mobilevanity.backend.data.Product;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class ProductListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String brandid = req.getParameter("brandid");
        Brand brand = null;
        if (!Utility.isEmpty(brandid)) {
            long id = Long.parseLong(brandid);
            brand = DataManager.getInstance().getBrand(id);
        }
        List<Product> products = DataManager.getInstance().listProduct(brand);
        if (products != null) {
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(products));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_UNKNOWN);
    }
}
