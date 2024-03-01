package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.feign.K8scliFeign;
import com.iscas.apiservice.mapper.ApiMapper;
import com.iscas.apiservice.mapper.DataSourceMapper;
import com.iscas.apiservice.mapper.GroupMapper;
import com.iscas.apiservice.mapper.ParamMapper;
import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.Parameter;
import com.iscas.apiservice.pojo.controllerToWeb.ApiAllDetail;
import com.iscas.apiservice.pojo.controllerToWeb.ApiListAndItsGroup;
import com.iscas.apiservice.pojo.controllerToWeb.ApiOutline;
import com.iscas.apiservice.pojo.controllerToWeb.ParamOutline;
import com.iscas.apiservice.pojo.feignToK8scli.CreateDeploymentInfo;
import com.iscas.apiservice.service.ApiService;
import com.iscas.apiservice.utils.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ParamMapper paramMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Setter
    @Autowired
    private K8scliFeign k8scliFeign;

    private static final int ERROR_CODE1 = -1;
    private static final int ERROR_CODE2 = -2;
    private static final int ERROR_CODE3 = -3;
    private static final int SPECIAL_CODE = 9;
    /**
     * 创建api.
     */
    @Override
    public int createApi(Api api) {
        // api表 对属性有两个约束：
        // 1. name不能重复
        // 2. group_id对应的分组必须存在
        try {
            if (groupMapper.getGroup(api.getGroupId()) == null) {
                return ERROR_CODE2;
            }
            apiMapper.createApi(api);
            return api.getApiId();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 接口列表.
     */
    @Override
    public ApiListAndItsGroup getApiList() {
        ApiListAndItsGroup apiListAndItsGroup = new ApiListAndItsGroup();
        Set<String> groups = new HashSet<>();
        ArrayList<Api> apiList = apiMapper.getApiList();
        ArrayList<ApiOutline> apiOutlineList = new ArrayList<>();
        for (Api api : apiList) {
            ApiOutline apiOutline = new ApiOutline(api);
            String groupName = groupMapper.getGroupNameById(api.getGroupId());
            apiOutline.setGroupName(groupName);
            groups.add(groupName);
            apiOutline.setSrcName(dataSourceMapper.getDataSourceByID(api.getSrcId()).getName());
            apiOutlineList.add(apiOutline);
            ArrayList<Parameter> parameterList = paramMapper.getParamsByApiId(api.getApiId());
            List<ParamOutline> paramOutlineList = parameterList.stream()
                .map(ParamOutline::new)
                .collect(Collectors.toList());
            apiOutline.setParams(paramOutlineList);
        }
        apiListAndItsGroup.setApiOutlines(apiOutlineList);
        apiListAndItsGroup.setGroups(groups);
        return apiListAndItsGroup;
    }

    /**
     * 根据id获取接口信息.
     */
    @Override
    public ApiAllDetail getApiInfoByApiId(int apiId) {
        Api api = apiMapper.getApiInfoByApiId(apiId);
        if (api == null) {
            return null;
        }
        // get parameter list
        ArrayList<Parameter> parameterList = paramMapper.getParamsByApiId(apiId);
        // params: list -> hashmap
        Map<String, ArrayList<String>> params = parameterList.stream()
            .collect(Collectors.toMap(
                Parameter::getName,
                parameter -> new ArrayList<>(Arrays.asList(parameter.getType(), parameter.getTitle()))
            ));
        ApiAllDetail apiAllDetail = new ApiAllDetail(api);
        apiAllDetail.setParams(params);
        apiAllDetail.setDataSource(dataSourceMapper.getDataSourceByID(api.getSrcId()));
        apiAllDetail.setGroup(groupMapper.getGroup(api.getGroupId()));
        return apiAllDetail;
    }

    /**
     * 通过数据服务组id获取组内接口列表.
     */
    @Override
    public Response getApiListByGroupId(int groupId) {
        if (groupMapper.getGroup(groupId) == null) {
            return Response.error().message("分组不存在");
        }
        return Response.ok().data("apiList", apiMapper.getApiListByGroupId(groupId));
    }

    /**
     * 获取接口代码.
     */
    @Override
    public String getCodeByApiId(int apiId) {
        return apiMapper.getCodeByApiId(apiId);
    }

    /**
     * 更新接口内容.
     */
    @Override
    public int updateApi(Api api) {
        // api not exist
        if (apiMapper.getApiInfoByApiId(api.getApiId()) == null) {
            return ERROR_CODE1;
        }
        // capture error：group not exist
        if (groupMapper.getGroup(api.getGroupId()) == null) {
            return ERROR_CODE2;
        }
        try {
            return apiMapper.updateApi(api);
        } catch (Exception e) {
            // capture duplicate name and unknown exceptions
            return ERROR_CODE3;
        }
    }

    /**
     * 删除接口.
     */
    @Override
    public int deleteApi(int apiId) {
        return apiMapper.deleteApi(apiId);
    }

    /**
     * 更新接口内容.
     */
    @Override
    @Transactional
    public Integer createApiAndAddParams(Api api, List<Parameter> params) {
        try {
            apiMapper.createApi(api);
        } catch (Exception e) {
            System.out.println("创建api时发生错误：" + e.getMessage());
            return ERROR_CODE1;
        }
        int apiID = api.getApiId();
        if (!params.isEmpty()) {
            // 判断params.name 是否有重复，若有重复返回-2
            Set<String> set = new HashSet<>();
            for (Parameter param : params) {
                param.setApiId(apiID);
                if (set.contains(param.getName())) {
                    return ERROR_CODE2;
                }
                set.add(param.getName());
            }
            paramMapper.addParams(params);
        }
        return apiID;
    }

    /**
     * 更新接口及参数.
     */
    @Override
    @Transactional
    public Integer updateApiAndParams(Api api, List<Parameter> params) {
        // 更新api异常
        if (updateApi(api) < 0) {
            return -1;
        }
        // 可能存在的异常是api不存在，但是在上一步已判断
        paramMapper.deleteParamsOfApi(api.getApiId());
        if (!params.isEmpty()) {
            paramMapper.addParams(params);
        }
        return 1;
    }

    /**
     * 删除接口及参数.
     */
    @Override
    public Integer deleteApiAndParams(int apiId) {
        if (apiMapper.getApiInfoByApiId(apiId) == null) {
            System.out.println("数据服务不存在");
            return -1;
        }
        apiMapper.deleteApi(apiId);
        paramMapper.deleteParamsOfApi(apiId);
        return 1;
    }

    /**
     * 发布服务接口.
     */
    @Override
    public int publishApi(Api api) {
        int apiId = api.getApiId();
        apiMapper.publishApi(apiId);
        return restartDeployment(api);
    }

    /**
     * 终止服务接口.
     */
    @Override
    public int terminateApi(Api api) {
        apiMapper.terminateApi(api.getApiId());
        try {
            String groupName = groupMapper.getGroupNameById(api.getGroupId());
            k8scliFeign.deleteDeployment(groupName);
        } catch (Exception e) {
            return ERROR_CODE3;   // 删除deployment发生错误
        }
        List<Integer> runningSvc = apiMapper.getRunningSvc(api.getGroupId());
        System.out.println(runningSvc.toString());
        if (runningSvc.isEmpty()) {  // 当前group中没有正在运行的api，直接返回
            return SPECIAL_CODE;
        }
        return restartDeployment(api);
    }

    /**
     * 重启Deployment.
     */
    public int restartDeployment(Api api) {
        CreateDeploymentInfo createDeploymentInfo =
            new CreateDeploymentInfo(dataSourceMapper.getDataSourceByID(api.getSrcId()).getName(),
                groupMapper.getGroupNameById(api.getGroupId()));
        try {
            k8scliFeign.createDeployment(createDeploymentInfo);
        } catch (Exception e) {
            System.out.println("创建或重启deployment时发生错误：" + e.getMessage());
            return ERROR_CODE2;
        }
        return 1;     // 实际上 没有相应deployment的时候直接不允许创建hpa，因此这里不需要进行额外判断
    }

    /**
     * 关键字过滤的接口列表.
     */
    @Override
    public ApiListAndItsGroup getApiListByKeyword(String keyword) {
        Set<String> groups = new HashSet<>();
        ArrayList<Api> apiList = apiMapper.getApiListByKeyword(keyword);
        ArrayList<ApiOutline> apiOutlineList = new ArrayList<>();
        for (Api api : apiList) {
            ApiOutline apiOutline = new ApiOutline(api);
            String groupName = groupMapper.getGroupNameById(api.getGroupId());
            apiOutline.setGroupName(groupName);
            groups.add(groupName);
            apiOutlineList.add(apiOutline);
            ArrayList<Parameter> parameterList = paramMapper.getParamsByApiId(api.getApiId());
            List<ParamOutline> paramOutlineList = parameterList.stream()
                .map(ParamOutline::new)
                .collect(Collectors.toList());
            apiOutline.setParams(paramOutlineList);
        }
        return new ApiListAndItsGroup(apiOutlineList, groups);
    }
}
