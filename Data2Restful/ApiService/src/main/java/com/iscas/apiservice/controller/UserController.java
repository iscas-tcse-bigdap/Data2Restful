package com.iscas.apiservice.controller;

import com.iscas.apiservice.service.UserService;
import com.iscas.apiservice.utils.ErrorModel;
import com.iscas.apiservice.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wbq
 * @version 1.0
 * @title UserController
 * @description
 * @create 2023/11/14 13:28
 */

@RestController
@RequestMapping(path = "/api/svc/user")
@Tag(name = "用户模块")
public final class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/inputAdviseUserList", method = RequestMethod.GET)
    @Operation(
        summary = "用户列表",
        description = "完整的用户列表，主要用于提供黑白名单输入建议",
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response inputAdviseUserList() {
        return userService.inputAdviseUserList();
    }
}
