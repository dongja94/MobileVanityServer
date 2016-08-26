package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Product;
import com.mobilevanity.backend.data.Sale;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class SaleInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String productIdString = req.getParameter("productid");
        String startDate = req.getParameter("startdate");
        String endDate = req.getParameter("enddate");
        Product product = null;
        Date start = null, end = null;
        if (!Utility.isEmpty(productIdString)) {
            if (Utility.isEmpty(type)) {
                type = "product";
            }
            if (type.equals("product")) {
                long id = Long.parseLong(productIdString);
                product = DataManager.getInstance().getProduct(id);
            }
        }
        if (!Utility.isEmpty(startDate)) {
            if (Utility.isEmpty(type)) {
                type = "date";
            }
            if (type.equals("date")) {
                try {
                    start = Utility.convertStringToDate(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!Utility.isEmpty(endDate)) {
            if (Utility.isEmpty(type)) {
                type = "date";
            }
            if (type.equals("date")) {
                try {
                    end = Utility.convertStringToDate(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (type.equals("product")) {
            if (product != null) {
                List<Sale> sales = DataManager.getInstance().findSaleByProduct(product);
                for (Sale sale : sales) {
                    sale.product = null;
                }
                Utility.responseSuccessMessage(resp, Utility.convertResponseList(sales));
                return;
            }
        } else if (type.equals("date")) {
            if (start != null || end != null) {
                List<Sale> sales = DataManager.getInstance().findSaleByDate(start, end);
                Utility.responseSuccessMessage(resp, Utility.convertResponseList(sales));
                return;
            }
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
