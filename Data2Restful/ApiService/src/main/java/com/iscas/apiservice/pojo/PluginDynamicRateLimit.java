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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author wbq
 * @version 1.0
 * @title PluginDynamicRateLimit
 * @description
 * @create 2023/11/9 15:33
 */

@Data
@TableName(value = "plugin_dynamic_rate_limit")
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class PluginDynamicRateLimit {
    @Id
    @JsonProperty(value = "rateLimitId")
    @TableId(value = "rate_limit_id", type = IdType.AUTO)
    @Schema(description = "rate-limit插件ID")
    private int rateLimitId;

    @Schema(example = "1", description = "数据服务组ID")
    private int groupId;

    @Schema(example = "5", description = "最大访问次数")
    private int rateLimit;

    @Schema(example = "60", description = "访问时间窗口")
    private int timeWindow;

    @Schema(example = "0", description = "插件状态，初始一定为0")
    private int status;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
