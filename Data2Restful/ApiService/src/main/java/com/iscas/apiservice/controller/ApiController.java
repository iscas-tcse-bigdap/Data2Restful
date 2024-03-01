package com.iscas.apiservice.controller;

import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.webToController.ApiAndParam;
import com.iscas.apiservice.pojo.webToController.BatchImport;
import com.iscas.apiservice.service.impl.ApiServiceImpl;
import com.iscas.apiservice.utils.ErrorModel;
import com.iscas.apiservice.utils.Response;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/api/svc")
@Tag(name = "数据接口")
public final class ApiController {
    @Autowired
    private ApiServiceImpl apiService;

    private static final int ERROR_CODE1 = -1;
    private static final int ERROR_CODE2 = -2;
    private static final int ERROR_CODE3 = -3;
    private static final int SPECIAL_CODE = 9;

    /**
     * @Deprecated 已弃用！！
     * This method was used to initially create the api interface of the project, but due to the need to atomically
     * associate the api interface with the corresponding parameters, this method has not been used.
     * Instead, you should use the /api/svc/publish/save interface in PublishController.
     */
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @Hidden
    public Response createApi(@RequestBody @Valid Api api) {
        if (!api.getName().matches("[a-z0-9-]+")) {
            return Response.error().message("服务名称只能包含小写英文字母、数字和中划线");
        }
        if (!Character.isLowerCase(api.getName().charAt(0))) {
            return Response.error().message("服务名称需由小写字母开头");
        }
        int result = apiService.createApi(api);
        if (result == ERROR_CODE1) {
            return Response.error().message("已存在该数据服务");
        } else if (result == ERROR_CODE2) {
            return Response.error().message("创建失败");
        } else {
            return Response.ok().data("ID", result);
        }
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    @Operation(
        summary = "新建数据接口",
        description = "给出完整具体的数据接口信息，新建数据服务接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中共包含了数据接口的所有信息，url需根据数据服务分组和接口名称严格按照格式生成，若code中需要参数，也应当完整标识在params中",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiAndParam.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response saveApi(@RequestBody @Valid ApiAndParam apiAndParam) {
        if (!apiAndParam.getName().matches("[a-z0-9-]+")) {
            return Response.error().message("服务名称只能包含小写英文字母、数字和中划线");
        }
        if (!Character.isLowerCase(apiAndParam.getName().charAt(0))) {
            return Response.error().message("服务需由小写字母开头");
        }
        Integer result = apiService.createApiAndAddParams(new Api(apiAndParam), apiAndParam.getParams());
        if (result == ERROR_CODE1) {
            return Response.error().data("msg", "创建api时发生错误");
        } else if (result == ERROR_CODE2) {
            return Response.error().data("msg", "添加参数时发生错误");
        }
        return Response.ok().data("apiID", result).data("params", apiAndParam.getParams());
    }


    @RequestMapping(path = "/getApiInfoByApiId", method = RequestMethod.GET)
    @Operation(
        summary = "根据接口ID获取接口信息",
        description = "操作的详细描述",
        parameters = {
            @Parameter(name = "apiId", description = "接口ID", required = true),
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response getApiInfoByApiId(@RequestParam("apiId") int apiId) {
        return Response.ok().data("api", apiService.getApiInfoByApiId(apiId));
    }

    @RequestMapping(path = "/getApiList", method = RequestMethod.GET)
    @Operation(
        summary = "接口列表",
        description = "获取完整的接口列表信息",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response getApiList() {
        return Response.ok().data("apiOutlineAndGroup", apiService.getApiList());
    }

    @RequestMapping(path = "/getApiListByGroupId", method = RequestMethod.GET)
    @Operation(
        summary = "分组接口列表",
        description = "获取一个数据服务分组的接口列表信息",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务分组ID", required = true),
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response getApiListByGroupId(@RequestParam("groupId") int groupId) {
        return apiService.getApiListByGroupId(groupId);
    }

    @RequestMapping(path = "/code", method = RequestMethod.GET)
    @Operation(
        summary = "接口代码",
        description = "根据数据服务接口ID获取接口代码",
        parameters = {
            @Parameter(name = "apiId", description = "数据服务接口ID", required = true),
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response getCodeByApiId(@RequestParam("apiId") int apiId) {
        return Response.ok().data("code", apiService.getCodeByApiId(apiId));
    }

    /**
     * <p>
     * This interface is only suitable for scenarios where only the API interface is modified without modifying
     * parameter information.
     * </p>
     * <p>
     * If you need to modify the interface information and parameter information at the same time, we recommend
     * using the /api/svc/publish/update interface in PublishController
     * </p>
     * <p>
     * Warning: we highly do not recommend that you use two interfaces at the same time to modify the api interface
     * and parameter information respectively.
     * </p>
     */
    @RequestMapping(path = "/update_api", method = RequestMethod.PUT)
    @Hidden
    public Response updateApi(@RequestBody @Valid Api api) {
        int result = apiService.updateApi(api);
        if (result == 1) {
            return Response.ok();
        } else {
            return Response.error().message("修改失败");
        }
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    @Operation(
        summary = "更新数据接口",
        description = "基本上与新建接口的要求一致：给出完整具体的数据接口信息，更新数据服务接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中共包含了数据接口的所有信息，url需根据数据服务分组和接口名称严格按照格式生成，若code中需要参数，也应当完整标识在params中",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiAndParam.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response updateApi(@RequestBody @Valid ApiAndParam apiAndParam) {
        if (!apiAndParam.getName().matches("[a-z0-9-]+")) {
            return Response.error().message("服务名称只能包含小写英文字母、数字和中划线");
        }
        if (!Character.isLowerCase(apiAndParam.getName().charAt(0))) {
            return Response.error().message("服务需由小写字母开头");
        }
        Integer result = apiService.updateApiAndParams(new Api(apiAndParam), apiAndParam.getParams());
        if (result == ERROR_CODE1) {
            return Response.error().data("msg", "更新服务信息时发生错误");
        } else if (result == ERROR_CODE2) {
            return Response.error().data("msg", "删除原参数时发生错误");
        } else if (result == ERROR_CODE3) {
            return Response.error().data("msg", "添加新参数时发生错误");
        }
        return Response.ok().data("params", apiAndParam.getParams());
    }

    /**
     * @Deprecated ---- deprecated due to the atomic requirements for api interface and parameter information
     */
    @RequestMapping(path = "/delete_api", method = RequestMethod.DELETE)
    @Hidden
    public Response deleteApiDeprecated(@RequestParam("apiId") int apiId) {
        int result = apiService.deleteApi(apiId);
        if (result == 1) {
            return Response.ok();
        } else {
            return Response.error().message("删除失败");
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除数据接口",
        description = "根据接口ID删除数据接口",
        parameters = {
            @Parameter(name = "apiId", description = "数据服务接口ID", required = true),
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response deleteApi(@RequestParam("apiId") int apiId) {
        Integer res = apiService.deleteApiAndParams(apiId);
        if (res == ERROR_CODE1) {
            return Response.error().data("msg", "数据服务不存在");
        } else if (res == ERROR_CODE2) {
            return Response.error().data("msg", "删除数据服务时发生错误");
        } else if (res == ERROR_CODE3) {
            return Response.error().data("msg", "删除参数时时发生错误");
        }
        return Response.ok().message("删除成功");
    }

    @RequestMapping(path = "/publish", method = RequestMethod.PUT)
    @Operation(
        summary = "发布",
        description = "根据数据apiId发布数据服务接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "必要的项是apiId，也只有apiId会起作用",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Api.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response publishApi(@RequestBody Api api) {
        int result = apiService.publishApi(api);
        if (result == 1) {
            return Response.ok();
        } else if (result == ERROR_CODE1) {
            return Response.error().message("更新接口状态发生错误");
        } else if (result == ERROR_CODE2) {
            return Response.error().message("创建或重启deployment时发生错误");
        } else if (result == ERROR_CODE3) {
            return Response.error().message("创建hpa时发生错误");
        } else {
            return Response.error().message("未知错误");
        }
    }

    @RequestMapping(path = "terminate", method = RequestMethod.PUT)
    @Operation(
        summary = "终止",
        description = "根据数据apiId终止数据服务接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "必要的项是apiId，也只有apiId会起作用",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Api.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response terminateApi(@RequestBody Api api) {
        int result = apiService.terminateApi(api);
        if (result == 1) {
            return Response.ok();
        } else if (result == SPECIAL_CODE) {
            return Response.ok().message("终止成功，当前服务（分组）无运行中的api接口，已删除service和pod");
        } else if (result == ERROR_CODE1) {
            return Response.error().message("更新接口状态发生错误");
        } else if (result == ERROR_CODE2) {
            return Response.error().message("创建或重启deployment时发生错误");
        } else if (result == ERROR_CODE3) {
            return Response.error().message("删除deployment发生错误");
        } else {
            return Response.error().message("终止失败，未知错误");
        }
    }

    @RequestMapping(path = "/filterByKeyword", method = RequestMethod.GET)
    @Operation(
        summary = "关键字接口列表",
        description = "根据关键字获取匹配的接口列表",
        parameters = {
            @Parameter(name = "keyword", description = "关键字", required = true),
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response filterListByKeyword(@RequestParam("keyword") String keyword) {
        return Response.ok().data("apiListWithKeyword", apiService.getApiListByKeyword(keyword));
    }

    @RequestMapping(path = "/createManyApi", method = RequestMethod.POST)
    @Operation(
        summary = "创建多个接口",
        description = "根据关键字获取匹配的接口列表（主要应用于导入api文档，批量创建接口，可扩展实现）",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "必要的项是apiId，也只有apiId会起作用",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatchImport.class, type = "array")
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    @Hidden
    public Response createManyApi(@RequestBody @Valid List<BatchImport> batchImportList) {
        return Response.ok();
    }
}
