package com.mobilevanity.backend.common;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class Utility {
    static Gson gson = new Gson();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static Date convertStringToDate(String text) throws ParseException {
        return sdf.parse(text);
    }

    public static String convertDateToString(Date date) {
        return sdf.format(date);
    }

    public static <T> void responseMessage(HttpServletResponse resp, int code, T data, String error) throws IOException {
        Result<T> result = new Result<>();
        result.code = code;
        result.result = data;
        result.error = error;
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(gson.toJson(result));
    }
    public static <T> void responseSuccessMessage(HttpServletResponse resp, T data) throws IOException {
        responseMessage(resp, Result.SUCCESS, data, null);
    }

    public static void responseErrorMessage(HttpServletResponse resp, int index) throws IOException {
        responseMessage(resp, Result.ERROR_INFOS[index].code, null, Result.ERROR_INFOS[index].message);
    }

    public static void responseErrorMessage(HttpServletResponse resp, int errorNo, String message) throws IOException {
        responseMessage(resp, errorNo, null, message);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.equals("");
    }

    public static<T,S extends DataConverter<T>> List<T> convertResponseList(List<S> converts, Object... args) {
        List<T> list = new ArrayList<>();
        for (S converter : converts) {
            list.add(converter.convertResponse(args));
        }
        return list;
    }
}
