package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.CosmeticItem;
import com.mobilevanity.backend.data.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class CosmeticItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String cosmeticitemid = req.getParameter("cosmeticitemid");
            CosmeticItem item = null;
            if (!Utility.isEmpty(cosmeticitemid)) {
                long id = Integer.parseInt(cosmeticitemid);
                item = DataManager.getInstance().getCosmeticItem(id);
            }
            if (item != null) {
                if (item.owner.equals(Ref.create(user))) {
                    Utility.responseSuccessMessage(resp, item.convertResponse());
                    return;
                }
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
