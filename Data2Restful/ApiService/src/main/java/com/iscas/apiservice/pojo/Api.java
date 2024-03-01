package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iscas.apiservice.pojo.webToController.ApiAndParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "api")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Tag(name = "接口", description = "接口实体类")
public final class Api implements Serializable {
    /**
     * 数据服务接口id.
     */
    @Id
    @JsonProperty(value = "apiId")
    @TableId(value = "api_id", type = IdType.AUTO)
    @Schema(description = "接口ID")
    private int apiId;
    /**
     * 数据服务组ID.
     */
    @Schema(example = "1", description = "数据服务组ID")
    private int groupId;
    /**
     * 数据源ID.
     */
    @Schema(example = "1", description = "数据源ID")
    private int srcId;
    /**
     * 接口名称.
     */
    @Schema(example = "example-api", description = "接口名称")
    private String name;
    /**
     * 接口描述.
     */
    @Schema(example = "example-api-desc", description = "接口描述")
    private String apiDesc;
    /**
     * 接口代码.
     */
    @Schema(
        example = "select * from user\n"
            + "<trim prefix=\"where\" prefixOverrides=\"and\">\n"
            + "    <if test=\"'username' in params\" >\n"
            + "        username = #{username}\n"
            + "    </if>\n"
            + "</trim> ",
        description = "接口代码"
    )
    private String code;
    /**
     * 访问路径.
     */
    @Schema(example = "http://60.245.208.50:9001/svc/example-group-name/example-api-name", description = "访问路径")
    private String url;
    /**
     * 接口访问量.
     */
    @Schema(description = "接口访问量（暂未使用）")
    private int insNum;
    /**
     * 接口实例数.
     */
    @Schema(description = "接口实例数（暂未使用）")
    private int initInstanceNum;
    /**
     * 接口状态.
     */
    @Schema(example = "1", description = "接口状态")
    private int status;
    /**
     * 最近更新时间.
     */
    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最近更新时间")
    private Date updateTime;

    @Schema(example = "1", description = "是否公开")
    private int isPublic;

    public Api(ApiAndParam apiAndParam) {
        this.apiId = apiAndParam.getApiId();
        this.groupId = apiAndParam.getGroupId();
        this.srcId = apiAndParam.getSrcId();
        this.name = apiAndParam.getName();
        this.apiDesc = apiAndParam.getApiDesc();
        this.code = apiAndParam.getCode();
        this.url = apiAndParam.getUrl();
        this.isPublic = apiAndParam.getIsPublic();
    }

    public Api(ApiAndParam apiAndParam, Date time) {
        this.apiId = apiAndParam.getApiId();
        this.groupId = apiAndParam.getGroupId();
        this.srcId = apiAndParam.getSrcId();
        this.name = apiAndParam.getName();
        this.apiDesc = apiAndParam.getApiDesc();
        this.code = apiAndParam.getCode();
        this.url = apiAndParam.getUrl();
        this.isPublic = apiAndParam.getIsPublic();
        this.updateTime = time;
    }

    public Api setNameAndReturn(String newName) {
        this.name = newName;
        return this;
    }
}
