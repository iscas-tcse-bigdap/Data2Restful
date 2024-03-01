package com.example.gateway.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Id;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimit
 * @description
 * @create 2023/11/2 11:00
 */
@Data
@Component
public class RateLimit {
    @Id
    @JsonProperty(value = "rateLimitId")
    @TableId(value = "rate_limit_id", type = IdType.AUTO)
    private int rateLimitId;
    private int groupId;
    private int rateLimit;
    private int timeWindow;
    private int status;
    private String name;
}
