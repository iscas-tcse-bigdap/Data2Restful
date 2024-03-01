package com.iscas.apiservice.pojo.webToController;

import com.iscas.apiservice.pojo.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *@title ApiDetailAndParams
 *@description  this entity class is to batch import data services
 *@author wbq
 *@version 1.0
 *@create 2023/9/25 20:12
 */
@Data
public class BatchImport {
    @Schema(example = "1", description = "数据服务组ID")
    private int groupId;

    @Schema(example = "1", description = "数据源ID")
    private int srcId;

    @Schema(example = "example-api", description = "接口名称")
    private String name;

    @Schema(example = "example-api-desc", description = "接口描述")
    private String apiDesc;

    @Schema(example = "select * from user\n"
        + "<trim prefix=\"where\" prefixOverrides=\"and\">\n"
        + "    <if test=\"'username' in params\" >\n"
        + "        username = #{username}\n"
        + "    </if>\n"
        + "</trim> ", description = "接口代码")
    private String code;

    @Schema(example = "http://60.245.208.50:9001/svc/example-group-name/example-api-name", description = "访问路径")
    private String url;
    private Date updateTime;

    @Schema(example = "[{\"name\":\"username\",\"type\":\"string\"}]", description = "参数列表")
    private List<Parameter> params;
}
