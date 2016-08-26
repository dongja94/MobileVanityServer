package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Cosmetic;
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
public class CosmeticListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productid = req.getParameter("productid");
        Product product = null;
        if (!Utility.isEmpty(productid)) {
            long id = Long.parseLong(productid);
            product = DataManager.getInstance().getProduct(id);
        }
        List<Cosmetic> cosmetics = DataManager.getInstance().listCosmetic(product);
        if (cosmetics != null) {
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(cosmetics));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_UNKNOWN);
    }
}
