package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@TableName(value = "params")
@NoArgsConstructor
public class Parameter {
    @Id
    @JsonProperty(value = "paramId")
    @TableId(value = "param_id", type = IdType.AUTO)
    @Schema(description = "接口参数ID")
    private int paramId;

    @Schema(example = "1", description = "数据接口ID")
    private int apiId;

    @Schema(example = "example-param-name", description = "参数名称")
    private String name;

    @Schema(example = "string", description = "参数类型")
    private String type;

    @Schema(example = "example-param-default-value", description = "参数默认值")
    private String defaultValue;

    @Schema(example = "example-param-title", description = "参数描述")
    private String title;

    public Parameter(String newName, String newType, String newDefaultValue, String newTitle) {
        this.name = newName;
        this.type = newType;
        this.defaultValue = newDefaultValue;
        this.title = newTitle;
    }
}
