package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EntityListeners;
import java.util.Date;

@Data
@TableName(value = "apigroup")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Group {
    @JsonProperty(value = "groupId")
    @TableId(value = "group_id", type = IdType.AUTO)
    @Schema(description = "数据服务组ID")
    private int groupId;

    @Schema(example = "1", description = "数据源ID")
    private int srcId;

    @Schema(example = "example-group", description = "数据服务组名称")
    private String name;

    @Schema(example = "example-group-desc", description = "数据服务组描述")
    private String groupDesc;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(example = "0", description = "数据服务组是否有key-auth插件，初始一定为0")
    private int keyAuthStatus;

    @Schema(example = "0", description = "数据服务组是否有acl插件，初始一定为0")
    private int aclStatus;

    @Schema(example = "0", description = "数据服务组是否有rate-limit插件，初始一定为0")
    private int rateLimitStatus;

    @Schema(example = "0", description = "数据服务组是否有dynamic-route插件，初始一定为0")
    private int dynamicStatus;

    public Group(int newGroupId, int newSrcId, String newName, String newGroupDesc, Date newCreateTime,
                 int newKeyAuthStatus, int newAclStatus, int newRateLimitStatus, int newDynamicStatus) {
        this.groupId = newGroupId;
        this.srcId = newSrcId;
        this.name = newName;
        this.groupDesc = newGroupDesc;
        this.createTime = newCreateTime;
        this.keyAuthStatus = newKeyAuthStatus;
        this.aclStatus = newAclStatus;
        this.rateLimitStatus = newRateLimitStatus;
        this.dynamicStatus = newDynamicStatus;
    }
}
