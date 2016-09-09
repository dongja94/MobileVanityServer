package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Notify;
import com.mobilevanity.backend.data.User;

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
public class NotifyListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String lastDate = req.getParameter("lastDate");
            Date date = null;
            if (!Utility.isEmpty(lastDate)) {
                try {
                    date = Utility.convertStringToDate(lastDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (date == null) {
                date = new Date(0);
            }
            List<Notify> notifies = DataManager.getInstance().findNotify(user, date);
            Utility.responseSuccessMessage(resp, Utility.convertResponseList(notifies));
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
