package com.iscas.k8scli.response;


import com.iscas.k8scli.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class ErrorHandler implements ResponseBodyAdvice<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private static final String SWAGGER = "swagger";

    private static final int SERVER_ERROR = 500;

    /**
     * 异常处理
     */
    @ExceptionHandler
    ErrorResponse handleAllException(HttpServletResponse response, Exception e) {
        LOGGER.error("----------------<异常>----------------");
        e.printStackTrace();
        response.setStatus(SERVER_ERROR);  //因为是服务器异常，http res都返回500
        if (e instanceof BaseException) {  //如果是系统定义的异常
            BaseException be = (BaseException) e;
            return new ErrorResponse(be.getCode(), be.getErrorMessage());
        } else {  //遇到的是未知异常
            return new ErrorResponse(SERVER_ERROR, "未知错误: " + e.getMessage());
        }
    }

    /**
     * @param returnType the return type
     * @param converterType the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> controllerClass = returnType.getDeclaringClass();
        return controllerClass != this.getClass() && !controllerClass.getName().contains(SWAGGER);
    }

    /**
     * body写入前处理
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        return body;
    }
}
