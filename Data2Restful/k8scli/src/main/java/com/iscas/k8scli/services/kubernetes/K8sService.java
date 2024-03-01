package com.iscas.k8scli.services.kubernetes;


import com.iscas.k8scli.response.Result;

public interface K8sService {
    Result runApp(String name);
}
