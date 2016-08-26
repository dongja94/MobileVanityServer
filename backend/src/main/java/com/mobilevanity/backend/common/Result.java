package com.mobilevanity.backend.common;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class Result<T> {
    public static final int ERROR_NOT_LOGIN = 0;
    public static final int ERROR_NOT_FOUND = 1;
    public static final int ERROR_INVALID_ARGUMENT = 2;
    public static final int ERROR_UNKNOWN = 3;

    public static class ErrorInfo {
        public int code;
        public String message;
        public ErrorInfo(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static final ErrorInfo[] ERROR_INFOS = {
            new ErrorInfo(-1, "not login"),
            new ErrorInfo(-2, "not found"),
            new ErrorInfo(-3, "invalid argument"),
            new ErrorInfo(-4, "unknown")};

    public static final int SUCCESS = 1;
    public static final int NEED_SIGNUP = 2;
    public int code;
    public T result;
    public String error;
}
