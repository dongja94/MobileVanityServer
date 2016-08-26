package com.mobilevanity.backend;

import com.mobilevanity.backend.common.CosmeticConstant;
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
public class DeleteCosmeticItemServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            String itemid = req.getParameter("cosmeticitemid");
            CosmeticItem item = null;
            if (!Utility.isEmpty(itemid)) {
                long id = Long.parseLong(itemid);
                item = DataManager.getInstance().getCosmeticItem(id);
            }
            if (item != null && (item.owner.get().id == user.id)) {
                switch (item.cosmetic.get().product.get().category) {
                    case CosmeticConstant.CATEGORY_EYE_MAKEUP:
                        user.eyeNum--;
                        break;
                    case CosmeticConstant.CATEGORY_LIP_MAKEUP:
                        user.lipNum--;
                        break;
                    case CosmeticConstant.CATEGORY_SKIN_CARE:
                        user.skinNum--;
                        break;
                    case CosmeticConstant.CATEGORY_FACE:
                        user.faceNum--;
                        break;
                    case CosmeticConstant.CATEGORY_CLEANSING:
                        user.cleansingNum--;
                        break;
                    case CosmeticConstant.CATEGORY_TOOL:
                        user.toolNum--;
                        break;
                }
                DataManager.getInstance().saveUser(user);
                req.getSession().setAttribute(SessionConstant.USER, user);
                DataManager.getInstance().deleteCosmeticItem(item);
                Utility.responseSuccessMessage(resp, item.convertResponse());
            }

            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
