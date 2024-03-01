package com.iscas.apiservice.controller;

import com.iscas.apiservice.pojo.Parameter;
import com.iscas.apiservice.service.ParamService;
import com.iscas.apiservice.utils.ErrorModel;
import com.iscas.apiservice.utils.Response;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/svc/param")
@Tag(name = "数据接口参数")
public final class ParamController {

    @Autowired
    private ParamService paramService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @Operation(
        summary = "添加参数",
        description = "根据完整的参数信息添加参数。我们虽然提供了这一接口，但我们仍建议规范使用添加数据服务接口的api。此接口实际上是一个失误后的补救措施",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的参数信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Parameter.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response addParam(@RequestBody @Valid Parameter parameter) {
        Integer result = paramService.addParam(parameter);
        if (result != null) {
            return Response.ok().data("paramID", result);
        } else {
            return Response.error().message("参数添加失败");
        }
    }

    @RequestMapping(path = "/test", method = RequestMethod.POST)
    @Hidden
    public Response test(@RequestBody @Valid ArrayList<Parameter> parameters) {
        return Response.ok().data("params", parameters);
    }

    @RequestMapping(path = "/adds", method = RequestMethod.POST)
    @Operation(
        summary = "添加多个参数",
        description = "与add接口相似。虽然提供了这一接口，但我们仍建议规范使用添加数据服务接口的api。此接口实际上是一个失误后的补救措施",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的参数信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Parameter.class, type = "array")
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response addParams(@RequestBody @Valid List<Parameter> parameters) {
        Integer result = paramService.addParams(parameters);
        if (result != null) {
            return Response.ok().data("paramList", result);
        } else {
            return Response.error().message("参数添加失败");
        }
    }

    @RequestMapping(path = "/params", method = RequestMethod.GET)
    @Operation(
        summary = "获取数据服务接口的参数列表",
        description = "根据apiId获取参数列表",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "apiId", description = "数据接口id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response getParamsByApiId(@RequestParam("apiId") int apiId) {
        Map<String, Object> params = paramService.getParamsByApiId(apiId);
        return Response.ok().data("params", params);
    }
}
