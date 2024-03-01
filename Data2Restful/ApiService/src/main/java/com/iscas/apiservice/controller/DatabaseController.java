package com.iscas.apiservice.controller;

import com.iscas.apiservice.pojo.DataSource;
import com.iscas.apiservice.pojo.FindDb;
import com.iscas.apiservice.pojo.controllerToWeb.DataSourceForElementSelect;
import com.iscas.apiservice.service.DatabaseService;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wbq
 * @version 1.0
 * @title DatabaseController
 * @description
 * @create 2023/9/13 15:19
 */

@RestController
@RequestMapping(path = "/api/svc/db")
@Tag(name = "数据源")
public final class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    private static final int ERROR_CODE1 = -1;
    private static final int ERROR_CODE2 = -2;

    @RequestMapping(path = "/currentTableList", method = RequestMethod.GET)
    @Operation(
        summary = "后端数据源中的表列表",
        description = "获取后端项目所使用数据源中的表列表",
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
    public Response getCurrentTableList() {
        List<String> tableList = databaseService.getCurrentTableList();
        return Response.ok().data("tableList", tableList);
    }

    @RequestMapping(path = "/tableList", method = RequestMethod.GET)
    @Operation(
        summary = "mysql数据源中的所有表",
        description = "获取mysql数据源中的所有表的列表",
        parameters = {
            @Parameter(name = "dburl", description = "数据源url", required = true),
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "password", description = "凭证", required = true),
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
    public Response getTableList(@RequestParam("dburl") String dbUrl,
                                 @RequestParam("username") String userName,
                                 @RequestParam("password") String passWord) throws SQLException {
        FindDb findDb = new FindDb();
        findDb.setDbUrl(dbUrl);
        findDb.setUserName(userName);
        findDb.setPassWord(passWord);
        findDb.setDriverClass("com.mysql.cj.jdbc.Driver");
        List<Map<String, Object>> tableList = databaseService.getTableList(findDb);
        return Response.ok().data("tableList", tableList);
    }

    @RequestMapping(path = "/mongoDictList", method = RequestMethod.GET)
    @Operation(
        summary = "mongodb数据源中的所有字典",
        description = "获取mongodb数据源中所有字典的列表",
        parameters = {
            @Parameter(name = "host", description = "主机", required = true),
            @Parameter(name = "port", description = "端口", required = true),
            @Parameter(name = "database", description = "数据库", required = true),
            @Parameter(name = "user", description = "用户", required = true),
            @Parameter(name = "pwd", description = "凭证", required = true),
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
    public Response getMongoDictList(@RequestParam("host") String host,
                                     @RequestParam("port") String port,
                                     @RequestParam("database") String database,
                                     @RequestParam("user") String user,
                                     @RequestParam("pwd") String pwd) {
        List<Map<String, Object>> mongoDictList = databaseService.getMongoDictList(host, port, database, user, pwd);
        return Response.ok().data("mongoDictList", mongoDictList);
    }

    @RequestMapping(path = "/sourceList", method = RequestMethod.GET)
    @Operation(
        summary = "数据源列表",
        description = "获取当前系统中已添加的数据源列表",
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
    public Response getDataSourceList() {
        ArrayList<DataSource> res = databaseService.getDataSourceList();
        return Response.ok().data("dataSourceList", res);
    }

    @RequestMapping(path = "/datasource", method = RequestMethod.GET)
    @Operation(
        summary = "数据源信息",
        description = "通过src_id获取数据源具体信息",
        parameters = {
            @Parameter(name = "srcId", description = "数据源id", required = true),
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
            @ApiResponse(description = "error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorModel.class)
                )
            )
        }
    )
    public Response getDataSourceById(@RequestParam("srcId") int srcId) {
        DataSource res = databaseService.getDataSourceByID(srcId);
        return Response.ok().data("dataSource", res);
    }

    @RequestMapping(path = "/dsForElementSelect", method = RequestMethod.GET)
    @Operation(
        summary = "适配element-ui组件库的数据源信息",
        description = "通过src_id获取数据源具体信息，并对获取到的数据源信息进行结构调整，以适配前端简便使用element-ui组件的需求",
        parameters = {
            @Parameter(name = "srcId", description = "数据源id", required = true),
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
    public Response getDsForElementSelectById(@RequestParam("srcId") int srcId) {
        DataSource res = databaseService.getDataSourceByID(srcId);
        return Response.ok().data("dataSource", new DataSourceForElementSelect(res));
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @Operation(
        summary = "添加数据源",
        description = "给出完整具体的数据源信息，添加数据源",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "请求体中应包含数据源的所有信息，只有rmark是一个可选的备注项，其他都是必填的。此外，status表示当前数据源的状态，"
                + "我们建议先使用testConnection连接测试数据源是否能连接，若能连接，status应为1，否则为0",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DataSource.class)
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
    public Response createDataSource(@RequestBody @Valid DataSource dataSource) {
        if (!dataSource.getName().matches("[a-z0-9-]+")) {
            return Response.error().message("服务名称只能包含小写英文字母、数字和中划线");
        }
        if (!Character.isLowerCase(dataSource.getName().charAt(0))) {
            return Response.error().message("服务名称需由小写字母开头");
        }
        int result = databaseService.createDataSource(dataSource);
        if (result == ERROR_CODE1) {
            return Response.error().message("已存在同名数据源");
        } else if (result == ERROR_CODE2) {
            return Response.error().message("创建失败");
        } else {
            return Response.ok().data("ID", result);
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    @Operation(
        summary = "移除数据源",
        description = "根据数据源id，移除数据源",
        parameters = {
            @Parameter(name = "srcId", description = "数据源id", required = true),
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
    public Response deleteDataSource(@RequestParam("srcId") int srcId) {
        int result = databaseService.deleteDataSource(srcId);
        if (result == 1) {
            return Response.ok();
        } else {
            return Response.error().message("删除失败");
        }
    }

    @RequestMapping(path = "/testConnectionMysql", method = RequestMethod.GET)
    @Operation(
        summary = "连接测试mysql数据源",
        description = "连接测试mysql数据源",
        parameters = {
            @Parameter(name = "url", description = "数据源url", required = true),
            @Parameter(name = "driverClass", description = "驱动类", required = true),
            @Parameter(name = "user", description = "用户", required = true),
            @Parameter(name = "pwd", description = "凭据", required = true),
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
    public Response testConnectionMysql(@RequestParam("url") String url,
                                        @RequestParam("driverClass") String driverClass,
                                        @RequestParam("user") String user,
                                        @RequestParam("pwd") String pwd) throws SQLException {
        return databaseService.testConnectionMysql(url, driverClass, user, pwd);
    }

    @RequestMapping(path = "/testConnectionMongoDB", method = RequestMethod.GET)
    @Operation(
        summary = "连接测试mongodb数据源",
        description = "连接测试mongodb数据源",
        parameters = {
            @Parameter(name = "host", description = "主机", required = true),
            @Parameter(name = "port", description = "端口", required = true),
            @Parameter(name = "database", description = "数据库", required = true),
            @Parameter(name = "user", description = "用户", required = true),
            @Parameter(name = "pwd", description = "凭据", required = true),
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
    public Response testConnectionMongoDB(@RequestParam("host") String host,
                                          @RequestParam("port") String port,
                                          @RequestParam("database") String database,
                                          @RequestParam("user") String user,
                                          @RequestParam("pwd") String pwd) {
        return databaseService.testConnectionMongoDB(host, port, database, user, pwd);
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    @Operation(
        summary = "更新数据源信息",
        description = "给出完整的数据源信息，更新数据源",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "这一接口要求给出完整的数据源信息。即使部分数据源信息未修改，也需要将原本的信息填入",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DataSource.class)
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
    public Response updateDataSource(@RequestBody @Valid DataSource dataSource) {
        int result = databaseService.updateDataSource(dataSource);
        if (result == 1) {
            return Response.ok();
        } else {
            return Response.error().message("数据源更新失败");
        }
    }
}
