package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.mapper.ParamMapper;
import com.iscas.apiservice.pojo.Parameter;
import com.iscas.apiservice.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class ParamServiceImpl implements ParamService {

    @Autowired
    private ParamMapper paramMapper;

    @Override
    public Integer addParam(Parameter param) {
        paramMapper.addParam(param);
        return param.getParamId();
    }

    @Override
    public Integer addParams(List<Parameter> params) {
        return paramMapper.addParams(params);
    }

    @Override
    public Map<String, Object> getParamsByApiId(int apiId) {
        Map<String, Object> paramsInfo = new HashMap<>();
        ArrayList<Parameter> parameterList = paramMapper.getParamsByApiId(apiId);
        for (Parameter param : parameterList) {
            paramsInfo.put(param.getName(), param);
        }
        return paramsInfo;
    }
}
