package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.CosmeticItem;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class CosmeticItemsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            int category = 0;
            try {
                category = Integer.parseInt(req.getParameter("category"));
            } catch (Exception e) {

            }
            int item = 0;
            try {
                item = Integer.parseInt(req.getParameter("item"));
            } catch (Exception e) {

            }
            List<CosmeticItem> items = DataManager.getInstance().findCosmeticItem(user, category, item);
            for (CosmeticItem ci : items) {
                ci.owner = null;
            }
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(items));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
