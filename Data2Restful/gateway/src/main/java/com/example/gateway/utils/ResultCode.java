package com.example.gateway.utils;

public final class ResultCode {
    private ResultCode() { }
    public static final int SUCCESS = 20000;

    public static final int ERROR = 20001;

    public static final int PASS = 20200;   // 放行状态码

    public static final int UNAUTHORIZED = 40001;

    public static final int FORBIDDEN = 40003;

    public static final int TOOMANYREQUEST = 40029;

}
