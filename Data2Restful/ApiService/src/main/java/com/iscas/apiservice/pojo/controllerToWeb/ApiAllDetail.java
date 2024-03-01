package com.iscas.apiservice.pojo.controllerToWeb;

import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.DataSource;
import com.iscas.apiservice.pojo.Group;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

/**
 *@title ApiAndParamMap
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/8/22 16:55
 */

@Data
@NoArgsConstructor
public class ApiAllDetail {
    private int groupId;
    private int srcId;
    private String name;
    private String apiDesc;
    private String code;
    private String url;
    private int status;
    private int isPublic;

    private Map<String, ArrayList<String>> params;
    private DataSource dataSource;
    private Group group;

    public ApiAllDetail(Api api) {
        this.groupId = api.getGroupId();
        this.srcId = api.getSrcId();
        this.name = api.getName();
        this.apiDesc = api.getApiDesc();
        this.code = api.getCode();
        this.url = api.getUrl();
        this.status = api.getStatus();
        this.isPublic = api.getIsPublic();
    }

    public ApiAllDetail(int newGroupId, int newSrcId, String newName, String desc) {
        this.groupId = newGroupId;
        this.srcId = newSrcId;
        this.name = newName;
        this.apiDesc = desc;
    }
}
