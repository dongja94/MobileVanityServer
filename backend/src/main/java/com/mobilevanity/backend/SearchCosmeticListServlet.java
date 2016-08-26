package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Brand;
import com.mobilevanity.backend.data.Cosmetic;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class SearchCosmeticListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String brandid = req.getParameter("brandid");
        String categoryText = req.getParameter("category");
        String itemText = req.getParameter("item");
        String keyword = req.getParameter("keyword");
        Brand brand = null;
        int category = 0, item = 0;
        try {
            if (!Utility.isEmpty(brandid)) {
                long id = Long.parseLong(brandid);
                brand = DataManager.getInstance().getBrand(id);
            }
            if (!Utility.isEmpty(categoryText)) {
                category = Integer.parseInt(categoryText);
            }
            if (!Utility.isEmpty(itemText)) {
                item = Integer.parseInt(itemText);
            }

            if (brand != null || category > 0 || item > 0 || !Utility.isEmpty(keyword)) {
                List<Cosmetic> cosmetics = DataManager.getInstance().findCosmetic(brand, category, item, keyword);
                Utility.responseSuccessMessage(resp, Utility.convertResponseList(cosmetics));
                return;
            }
        } catch (Exception e) {
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
