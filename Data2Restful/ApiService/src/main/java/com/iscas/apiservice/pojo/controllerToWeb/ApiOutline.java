package com.iscas.apiservice.pojo.controllerToWeb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.apiservice.pojo.Api;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ApiOutline {
    private int apiId;
    private String apiName;
    private int groupId;
    private String groupName;
    private int srcId;
    private String srcName;

    private String url;
    private int status;
    private String apiDesc;
    private String code;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private int isPublic;
    private List<ParamOutline> params;

    public ApiOutline(Api api) {
        this.apiId = api.getApiId();
        this.apiName = api.getName();
        this.groupId = api.getGroupId();
        this.srcId = api.getSrcId();
        this.url = api.getUrl();
        this.status = api.getStatus();
        this.apiDesc = api.getApiDesc();
        this.updateTime = api.getUpdateTime();
        this.isPublic = api.getIsPublic();
    }
}
