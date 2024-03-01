package com.iscas.apiservice.controller;

import com.iscas.apiservice.pojo.Group;
import com.iscas.apiservice.pojo.PluginAcl;
import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import com.iscas.apiservice.pojo.PluginKeyAuth;
import com.iscas.apiservice.pojo.controllerToWeb.AclUserIdName;
import com.iscas.apiservice.pojo.dbTemplate.CreateAcl;
import com.iscas.apiservice.pojo.dbTemplate.CreateAclDetailItem;
import com.iscas.apiservice.pojo.webToController.AclWithId;
import com.iscas.apiservice.service.GroupService;
import com.iscas.apiservice.utils.ErrorModel;
import com.iscas.apiservice.utils.Response;
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = "/api/svc/group")
@Tag(name = "数据服务/分组")
public final class GroupController {

    @Autowired
    private GroupService groupService;

    private static final int ERROR_CODE1 = -1;
    private static final int ERROR_CODE2 = -2;

    @RequestMapping(path = "/group", method = RequestMethod.POST)
    @Operation(
        summary = "创建数据服务分组",
        description = "给出完整具体的数据服务信息，添加数据服务分组",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含数据服务的所有信息，必要的信息是数据服务名称name、数据服务描述description和srcId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Group.class)
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
    public Response createGroup(@RequestBody @Valid Group group) {
        Integer result = groupService.createGroup(group);
        if (result != null) {
            if (result == ERROR_CODE1) {
                return Response.error().message("创建服务分组时发生错误");
            } else if (result == ERROR_CODE2) {
                return Response.error().message("创建ingress时发生错误");
            } else {
                return Response.ok().data("groupID", result);
            }
        } else {
            return Response.error().message("创建失败");
        }
    }

    @RequestMapping(path = "/getGroupList", method = RequestMethod.GET)
    @Operation(
        summary = "数据服务列表",
        description = "获取系统中数据服务分组的列表",
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
    public Response getGroupList() {
        return Response.ok().data("groupList", groupService.getGroupList());
    }

    @RequestMapping(path = "/group", method = RequestMethod.GET)
    @Operation(
        summary = "数据服务信息",
        description = "根据数据服务分组的id获取数据服务具体信息",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
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
    public Response getGroup(@RequestParam("groupId") int groupId) {
        return Response.ok().data("group", groupService.getGroup(groupId));
    }

    /**
     * this api is designed for gateway module to get group info by name using spring-openfeign.
     *
     * @param groupName
     * @return
     */
    @RequestMapping(path = "/groupByName", method = RequestMethod.GET)
    public Group getGroupByName(@RequestParam("groupName") String groupName) {
        return groupService.getGroupByName(groupName);
    }

    @RequestMapping(path = "/group", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除数据服务",
        description = "根据数据服务分组的 id 和 名称 移除数据服务分组",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
            @Parameter(name = "groupName", description = "数据服务名称", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response deleteGroup(@RequestParam("groupId") int groupId, @RequestParam("groupName") String groupName) {
        return groupService.deleteGroup(groupId, groupName);
    }

    @RequestMapping(path = "/group", method = RequestMethod.PUT)
    @Operation(
        summary = "更新数据服务",
        description = "给出完整的数据服务信息更新数据服务分组",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含数据服务的所有信息，必要的信息是数据服务名称name、数据服务描述description和srcId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Group.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateGroup(@RequestBody @Valid Group group) {
        int result = groupService.updateGroup(group);
        if (result == 1) {
            return Response.ok();
        } else {
            return Response.error().message("分组信息更新失败");
        }
    }


    /**
     * 以下均为插件部分的接口.
     */

    @RequestMapping(path = "/pluginList", method = RequestMethod.GET)
    @Operation(
        summary = "插件列表",
        description = "根据数据服务id，获取当前数据服务已添加的插件列表",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response getPluginList(@RequestParam("groupId") int groupId) {
        return groupService.getPluginList(groupId);
    }

    @RequestMapping(path = "/unallocatedPluginList", method = RequestMethod.GET)
    @Operation(
        summary = "未分配插件列表",
        description = "根据数据服务id，获取当前数据服务未分配的插件列表",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response getUnallocatedPluginList(@RequestParam("groupId") int groupId) {
        return groupService.getUnallocatedPluginList(groupId);
    }

    @RequestMapping(path = "/updateKeyAuthPlugin", method = RequestMethod.PUT)
    @Operation(
        summary = "更新KeyAuth插件",
        description = "给出完整的KeyAuth插件信息，更新插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含插件的完整信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Group.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateKeyAuthPlugin(@RequestBody @Valid PluginKeyAuth pluginKeyAuth)
        throws NoSuchAlgorithmException {
        return groupService.updateKeyAuthPlugin(pluginKeyAuth);
    }

    @RequestMapping(path = "/updateKeyAuthStatus", method = RequestMethod.PUT)
    @Operation(
        summary = "更新KeyAuth插件状态",
        description = "根据插件id更新插件状态",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中必要参数是插件Id",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Group.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateKeyAuthStatus(@RequestBody @Valid PluginKeyAuth pluginKeyAuth) {
        return groupService.updateKeyAuthStatus(pluginKeyAuth);
    }

    @RequestMapping(path = "/deleteKeyAuthPlugin", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除KeyAuth插件",
        description = "根据数据服务id，删除keyAuth插件",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response deleteKeyAuthPlugin(@RequestParam("groupId") int groupId) {
        return groupService.deleteKeyAuthPlugin(groupId);
    }

    @RequestMapping(path = "/addKeyAuthPlugin", method = RequestMethod.POST)
    @Operation(
        summary = "添加KeyAuth插件",
        description = "根据groupId添加插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含分组信息，必要的参数是groupId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginKeyAuth.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response addKeyAuthPlugin(@RequestBody @Valid PluginKeyAuth pluginKeyAuth) throws NoSuchAlgorithmException {
        return groupService.addKeyAuthPlugin(pluginKeyAuth);
    }

    @RequestMapping(path = "/createAclPlugin", method = RequestMethod.POST)
    @Operation(
        summary = "添加Acl插件",
        description = "根据groupId添加插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含分组信息，必要的参数是groupId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginAcl.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response createAclPlugin(@RequestBody @Valid PluginAcl pluginAcl) {
        return groupService.createAclPlugin(pluginAcl);
    }

    @RequestMapping(path = "/deleteAclPlugin", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除Acl插件",
        description = "根据groupId删除插件",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response deleteAclPlugin(@RequestParam("groupId") int groupId) {
        return groupService.deleteAclPlugin(groupId);
    }

    @RequestMapping(path = "/updateAclPlugin", method = RequestMethod.PUT)
    @Operation(
        summary = "更新Acl插件",
        description = "根据完整的acl信息，更新插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AclWithId.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateAclPlugin(@RequestBody @Valid AclWithId acl) {
        List<CreateAclDetailItem> newWhitelist = new ArrayList<>();
        List<CreateAclDetailItem> newBlacklist = new ArrayList<>();
        for (AclUserIdName whitelist : acl.getWhiteList()) {
            CreateAclDetailItem newWhite = new CreateAclDetailItem(whitelist.getUserId(), acl.getAclId());
            newWhitelist.add(newWhite);
        }
        for (AclUserIdName blacklist : acl.getBlackList()) {
            CreateAclDetailItem newBlack = new CreateAclDetailItem(blacklist.getUserId(), acl.getAclId());
            newBlacklist.add(newBlack);
        }
        CreateAcl newAcl = new CreateAcl(acl.getAclId(), newWhitelist, newBlacklist);
        return groupService.updateAclPlugin(newAcl);
    }

    @RequestMapping(path = "/updateAclStatus", method = RequestMethod.PUT)
    @Operation(
        summary = "更新Acl插件状态",
        description = "根据关键的acl信息，更新插件状态",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息，必要的信息是aclId和status",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AclWithId.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateAclStatus(@RequestBody @Valid PluginAcl acl) {
        return groupService.updateAclStatus(acl);
    }

    @RequestMapping(path = "/createRateLimitPlugin", method = RequestMethod.POST)
    @Operation(
        summary = "添加RateLimit插件",
        description = "根据完整的RateLimit信息，添加插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含必要的groupId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicRateLimit.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response createRateLimitPlugin(@RequestBody @Valid PluginDynamicRateLimit pluginDynamicRateLimit) {
        return groupService.createRateLimitPlugin(pluginDynamicRateLimit);
    }

    @RequestMapping(path = "/deleteRateLimitPlugin", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除RateLimit插件",
        description = "根据groupId删除插件",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response deleteRateLimitPlugin(@RequestParam("groupId") int groupId) {
        return groupService.deleteRateLimitPlugin(groupId);
    }

    @RequestMapping(path = "/updateRateLimitPlugin", method = RequestMethod.PUT)
    @Operation(
        summary = "更新RateLimit插件",
        description = "根据完整的RateLimit信息，更新插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicRateLimit.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateRateLimitPlugin(@RequestBody @Valid PluginDynamicRateLimit pluginDynamicRateLimit) {
        return groupService.updateRateLimitPlugin(pluginDynamicRateLimit);
    }

    @RequestMapping(path = "updateRateLimitStatus", method = RequestMethod.PUT)
    @Operation(
        summary = "更新RateLimit插件状态",
        description = "根据关键的RateLimit信息，更新插件状态",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息，必要的信息是RateLimitId和status",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicRateLimit.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateRateLimitStatus(@RequestBody @Valid PluginDynamicRateLimit pluginDynamicRateLimit) {
        return groupService.updateRateLimitStatus(pluginDynamicRateLimit);
    }

    @RequestMapping(path = "/createDynamicExpansionPlugin", method = RequestMethod.POST)
    @Operation(
        summary = "添加DynamicExpansion插件",
        description = "根据关键的DynamicExpansion信息，添加插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含必要的groupId",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicExpansion.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response createDynamicExpansionPlugin(@RequestBody @Valid PluginDynamicExpansion pluginDynamicExpansion) {
        return groupService.createDynamicExpansionPlugin(pluginDynamicExpansion);
    }

    @RequestMapping(path = "/deleteDynamicExpansionPlugin", method = RequestMethod.DELETE)
    @Operation(
        summary = "删除DynamicExpansion插件",
        description = "根据groupId删除插件",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response deleteDynamicExpansionPlugin(@RequestParam("groupId") int groupId) {
        return groupService.deleteDynamicExpansionPlugin(groupId);
    }

    @RequestMapping(path = "/updateDynamicExpansionPlugin", method = RequestMethod.PUT)
    @Operation(
        summary = "更新DynamicExpansion插件",
        description = "根据完整的DynamicExpansion信息，更新插件",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicExpansion.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateDynamicExpansionPlugin(@RequestBody @Valid PluginDynamicExpansion pluginDynamicExpansion) {
        return groupService.updateDynamicExpansionPlugin(pluginDynamicExpansion);
    }

    @RequestMapping(path = "/updateDynamicExpansionStatus", method = RequestMethod.PUT)
    @Operation(
        summary = "更新DynamicExpansion插件状态",
        description = "根据关键的DynamicExpansion信息，更新插件状态",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含完整的插件信息，必要的信息是DynamicExpansionId和status",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PluginDynamicRateLimit.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response updateDynamicExpansionStatus(@RequestBody @Valid PluginDynamicExpansion pluginDynamicExpansion) {
        return groupService.updateDynamicExpansionStatus(pluginDynamicExpansion);
    }

    /**
     * this api is designed for gateway module to get keyAuth status using spring-openfeign.
     */
    @RequestMapping(path = "/keyAuth", method = RequestMethod.GET)
    @Operation(
        summary = "获取keyAuth插件信息",
        description = "根据groupId获取keyAuth插件信息",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response getKeyAuth(@RequestParam("groupId") int groupId) {
        return groupService.getKeyAuth(groupId);
    }

    @RequestMapping(path = "/acl", method = RequestMethod.GET)
    @Operation(
        summary = "获取ACL插件信息",
        description = "根据groupId获取ACL插件信息",
        parameters = {
            @Parameter(name = "groupId", description = "数据服务id", required = true),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "error", content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorModel.class)))
        }
    )
    public Response getAcl(@RequestParam("groupId") int groupId) {
        return groupService.getAcl(groupId);
    }
}
