package com.iscas.apiservice.feign;

import com.iscas.apiservice.pojo.feignToK8scli.CreateDeploymentInfo;
import com.iscas.apiservice.pojo.feignToK8scli.CreateHpaInfo;
import com.iscas.apiservice.pojo.feignToK8scli.CreateIngressInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/*
 *@title k8scliFeign
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/19 10:46
 */
@FeignClient(name = "k8scli", path = "/api/k8s")
public interface K8scliFeign {
    @RequestMapping(path = "/createIngress", method = RequestMethod.POST)
    String createIngress(CreateIngressInfo createIngressInfo);

    @RequestMapping(path = "/deleteIngress", method = RequestMethod.DELETE)
    String deleteIngress(@RequestParam("groupName") String groupName);

    @RequestMapping(path = "/createDeployment", method = RequestMethod.POST)
    String createDeployment(CreateDeploymentInfo createDeploymentInfo);

    @RequestMapping(path = "/deleteDeployment", method = RequestMethod.DELETE)
    String deleteDeployment(@RequestParam("name") String name);

    @RequestMapping(path = "/createAutoAdjustReplica", method = RequestMethod.POST)
    String createAutoAdjustReplica(CreateHpaInfo createHpaInfo);

    @RequestMapping(path = "/updateAutoAdjustReplicaNum", method = RequestMethod.PUT)
    String updateAutoAdjustReplicaNum(CreateHpaInfo createHpaInfo);

    @RequestMapping(path = "/deleteAutoAdjustReplica", method = RequestMethod.DELETE)
    String deleteAutoAdjustReplica(@RequestParam("groupName") String groupName);
}
