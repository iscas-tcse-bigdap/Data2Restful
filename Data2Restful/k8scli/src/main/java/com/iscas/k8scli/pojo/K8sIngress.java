package com.iscas.k8scli.pojo;

import lombok.Data;

/**
 * @ClassName: K8sIngress
 * @Description:
 * @Author: wzc
 * @Date: 2023/8/24 9:43
 */
@Data
public class K8sIngress {

    private String namespace;
    private String name;
    private String path;
    private String labels;

}
