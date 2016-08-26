package com.mobilevanity.backend;

import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class BeautyTipInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String beautyTipId = req.getParameter("beautytipid");
        BeautyTip beautyTip = null;
        if (!Utility.isEmpty(beautyTipId)) {
            long id = Long.parseLong(beautyTipId);
            beautyTip = DataManager.getInstance().getBeautyTip(id);
        }
        if (beautyTip != null) {
            beautyTip.readNum++;
            DataManager.getInstance().saveBeautyTip(beautyTip);
            Utility.responseSuccessMessage(resp, beautyTip.convertResponse());
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
    }
}
