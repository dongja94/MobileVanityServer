package com.mobilevanity.backend;

import com.mobilevanity.backend.common.AdminConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.FAQ;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertFaqServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null && user.email.equals(AdminConstant.ADMIN_EMAIL)) {
            int number = Integer.parseInt(req.getParameter("number"));
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            if (!Utility.isEmpty(title) && !Utility.isEmpty(content)) {
                FAQ faq = new FAQ();
                faq.number = number;
                faq.title = title;
                faq.content = content;
                DataManager.getInstance().saveFAQ(faq);
                Utility.responseSuccessMessage(resp, faq);
                return;
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
