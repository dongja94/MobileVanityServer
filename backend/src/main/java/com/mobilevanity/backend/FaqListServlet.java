package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.FAQ;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class FaqListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FAQ> faqs = DataManager.getInstance().listFAQ();
        if (faqs != null) {
            Utility.responseSuccessMessage(resp, faqs);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
