package com.iscas.k8scli.exception;


public abstract class BaseException extends Exception {

    private int code;
    private String errorMessage;

    public BaseException(int newCode, String message) {
        super();
        this.code = newCode;
        this.errorMessage = message;
   }

    /**
     * 获取状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误信息
     */
    public BaseException withErrMessage(String message) {
        this.errorMessage = message;
        return this;
    }

    /**
     * @return toString
     */
    @Override
    public String toString() {
        return "BaseException{"
                + "code=" + code
                + ", errorMessage='" + errorMessage + '\''
                + '}';
    }
}
