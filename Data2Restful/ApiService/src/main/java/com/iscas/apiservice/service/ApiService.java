package com.iscas.apiservice.service;

import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.Parameter;
import com.iscas.apiservice.pojo.controllerToWeb.ApiAllDetail;
import com.iscas.apiservice.pojo.controllerToWeb.ApiListAndItsGroup;
import com.iscas.apiservice.utils.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiService {
    int createApi(Api api);

    ApiListAndItsGroup getApiList();

    ApiAllDetail getApiInfoByApiId(int apiId);

    Response getApiListByGroupId(int groupId);

    String getCodeByApiId(int apiId);

    int updateApi(Api api);

    int deleteApi(int apiId);

    Integer createApiAndAddParams(Api api, List<Parameter> params);

    Integer updateApiAndParams(Api api, List<Parameter> params);

    Integer deleteApiAndParams(int apiId);

    int publishApi(Api api);

    int terminateApi(Api api);

    ApiListAndItsGroup getApiListByKeyword(String keyword);

    interface ApiRepository extends JpaRepository<Api, Integer> {
    }
}

