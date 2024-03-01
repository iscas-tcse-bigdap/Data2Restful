package com.iscas.apiservice.pojo.webToController;

import com.iscas.apiservice.pojo.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ApiAndParam {
    @Schema(description = "接口ID")
    private int apiId;

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

    @Schema(example = "1", description = "是否公开")
    private int isPublic;

    @Schema(example = "[{\"name\":\"username\",\"type\":\"string\"}]", description = "参数列表")
    private List<Parameter> params;

    public ApiAndParam(int newApiId, int newGroupId, int newSrcId, String newName, String newApiDesc, String newCode,
                       String newUrl, int newIsPublic) {
        this.apiId = newApiId;
        this.groupId = newGroupId;
        this.srcId = newSrcId;
        this.name = newName;
        this.apiDesc = newApiDesc;
        this.code = newCode;
        this.url = newUrl;
        this.isPublic = newIsPublic;
    }
}
