package com.iscas.apiservice.pojo.controllerToWeb;

import com.iscas.apiservice.pojo.Parameter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParamOutline {
    private String paramName;
    private String paramType;

    public ParamOutline(Parameter parameter) {
        this.paramName = parameter.getName();
        this.paramType = parameter.getType();
    }
}
