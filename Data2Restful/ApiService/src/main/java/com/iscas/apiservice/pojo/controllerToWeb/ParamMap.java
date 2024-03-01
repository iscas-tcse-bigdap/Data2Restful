package com.iscas.apiservice.pojo.controllerToWeb;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/*
 *@title ParamMap
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/8/22 16:52
 */

@Data
public class ParamMap {
    private HashMap<String, ArrayList<String>> params;
}
