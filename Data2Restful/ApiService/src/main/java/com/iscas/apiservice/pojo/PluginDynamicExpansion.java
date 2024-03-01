package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author wbq
 * @version 1.0
 * @title PluginDynamicExpansion
 * @description
 * @create 2023/11/15 10:31
 */

@Data
@TableName(value = "plugin_dynamic_expansion")
@AllArgsConstructor
@NoArgsConstructor
public class PluginDynamicExpansion {
    @Id
    @JsonProperty(value = "dynamicExpansionId")
    @TableId(value = "dynamic_expansion_id", type = IdType.AUTO)
    @Schema(description = "动态扩容插件ID")
    private int dynamicExpansionId;

    @Schema(example = "1", description = "数据服务组ID")
    private int groupId;

    @Schema(example = "1", description = "最小副本数量")
    private int minValue;

    @Schema(example = "5", description = "最大副本数量")
    private int maxValue;

    @Schema(example = "0", description = "插件状态，初始一定为0")
    private int status;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
