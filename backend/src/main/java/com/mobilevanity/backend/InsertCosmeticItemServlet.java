package com.mobilevanity.backend;

import com.googlecode.objectify.Ref;
import com.mobilevanity.backend.common.CosmeticConstant;
import com.mobilevanity.backend.common.Result;
import com.mobilevanity.backend.common.SessionConstant;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.Cosmetic;
import com.mobilevanity.backend.data.CosmeticItem;
import com.mobilevanity.backend.data.User;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016-08-10.
 */
public class InsertCosmeticItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(SessionConstant.USER);
        if (user != null) {
            CosmeticItem item = new CosmeticItem();
            item.owner = Ref.create(user);
            long cosmeticid = Long.parseLong(req.getParameter("cosmeticid"));
            Cosmetic cosmetic = DataManager.getInstance().getCosmetic(cosmeticid);
            if (cosmetic != null) {
                item.cosmetic = Ref.create(cosmetic);
                item.cosmeticTerm = Integer.parseInt(req.getParameter("cosmeticTerm"));
                try {
                    item.dateAdded = Utility.convertStringToDate(req.getParameter("dateAdded"));
                    DataManager.getInstance().saveCosmeticItem(item);
                    item.owner = null;
                    switch (cosmetic.product.get().category) {
                        case CosmeticConstant.CATEGORY_EYE_MAKEUP :
                            user.eyeNum++;
                            break;
                        case CosmeticConstant.CATEGORY_LIP_MAKEUP :
                            user.lipNum++;
                            break;
                        case CosmeticConstant.CATEGORY_SKIN_CARE :
                            user.skinNum++;
                            break;
                        case CosmeticConstant.CATEGORY_FACE :
                            user.faceNum++;
                            break;
                        case CosmeticConstant.CATEGORY_CLEANSING :
                            user.cleansingNum++;
                            break;
                        case CosmeticConstant.CATEGORY_TOOL :
                            user.toolNum++;
                            break;
                    }
                    DataManager.getInstance().saveUser(user);
                    req.getSession().setAttribute(SessionConstant.USER, user);
                    Utility.responseSuccessMessage(resp, item.convertResponse());
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Utility.responseErrorMessage(resp, Result.ERROR_INVALID_ARGUMENT);
            return;
        }
        Utility.responseErrorMessage(resp, Result.ERROR_NOT_LOGIN);
    }
}
