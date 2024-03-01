package com.iscas.apiservice.service;

import com.iscas.apiservice.pojo.Parameter;

import java.util.List;
import java.util.Map;

public interface ParamService {
    Integer addParam(Parameter param);

    Integer addParams(List<Parameter> params);

    Map<String, Object> getParamsByApiId(int apiId);
}
