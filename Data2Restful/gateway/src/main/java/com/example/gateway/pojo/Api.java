package com.example.gateway.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
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
public class Api implements Serializable {
    @Id
    @JsonProperty(value = "apiId")
    @TableId(value = "api_id", type = IdType.AUTO)
    private int apiId;

    private int groupId;
    private int srcId;
    private String name;
    private String apiDesc;
    private String code;
    private String url;
    private int insNum;
    private int status;

    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private int isPublic;
}
