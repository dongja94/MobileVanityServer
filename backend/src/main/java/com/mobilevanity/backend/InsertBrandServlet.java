package com.mobilevanity.backend;

import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Brand;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertBrandServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            String name = req.getParameter("name");
            String images = req.getParameter("images");
            if (!Utility.isEmpty(name)) {
                Brand brand = new Brand();
                brand.name = name;
                brand.images = images;
                DataManager.getInstance().saveBrand(brand);
                Utility.responseSuccessMessage(resp, brand);
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
