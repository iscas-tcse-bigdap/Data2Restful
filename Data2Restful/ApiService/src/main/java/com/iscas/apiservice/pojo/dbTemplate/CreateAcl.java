package com.iscas.apiservice.pojo.dbTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title CreateAcl
 * @description
 * @create 2023/11/14 15:02
 */

@Data
@AllArgsConstructor
public class CreateAcl {
    private int aclId;
    private List<CreateAclDetailItem> whitelist;
    private List<CreateAclDetailItem> blacklist;
}
