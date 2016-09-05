package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Cosmetic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class CosmeticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cosmeticid = req.getParameter("cosmeticid");
        Cosmetic cosmetic = null;
        if (!Utility.isEmpty(cosmeticid)) {
            long id = Long.parseLong(cosmeticid);
            cosmetic = DataManager.getInstance().getCosmetic(id);
        }
        if (cosmetic != null) {
            Utility.responseSuccessMessage(resp, cosmetic.convertResponse());
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
