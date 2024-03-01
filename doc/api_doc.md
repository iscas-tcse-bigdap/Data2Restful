# 数据中台项目接口文挡


**简介**:数据中台项目接口文挡


**HOST**:http://60.245.208.50:8081


**联系人**:wbq


**Version**:1.0


**接口路径**:/v3/api-docs/数据中台


[TOC]






# 数据接口


## 更新数据接口


**接口地址**:`/api/svc/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>基本上与新建接口的要求一致：给出完整具体的数据接口信息，更新数据服务接口</p>



**请求示例**:


```javascript
{
  "apiId": 0,
  "groupId": 1,
  "srcId": 1,
  "name": "example-api",
  "apiDesc": "example-api-desc",
  "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
  "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
  "isPublic": 1,
  "params": [
    {
      "name": "username",
      "type": "string"
    }
  ]
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiAndParam|ApiAndParam|body|true|ApiAndParam|ApiAndParam|
|&emsp;&emsp;apiId|接口ID||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|接口名称||false|string||
|&emsp;&emsp;apiDesc|接口描述||false|string||
|&emsp;&emsp;code|接口代码||false|string||
|&emsp;&emsp;url|访问路径||false|string||
|&emsp;&emsp;isPublic|是否公开||false|integer(int32)||
|&emsp;&emsp;params|参数列表||false|array|Parameter|
|&emsp;&emsp;&emsp;&emsp;apiId|数据接口ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;name|参数名称||false|string||
|&emsp;&emsp;&emsp;&emsp;type|参数类型||false|string||
|&emsp;&emsp;&emsp;&emsp;defaultValue|参数默认值||false|string||
|&emsp;&emsp;&emsp;&emsp;title|参数描述||false|string||
|&emsp;&emsp;&emsp;&emsp;paramId|接口参数ID||false|integer||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 终止


**接口地址**:`/api/svc/terminate`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据apiId终止数据服务接口</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "srcId": 1,
  "name": "example-api",
  "apiDesc": "example-api-desc",
  "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
  "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
  "insNum": 0,
  "initInstanceNum": 0,
  "status": 1,
  "updateTime": "",
  "isPublic": 1,
  "nameAndReturn": {
    "groupId": 1,
    "srcId": 1,
    "name": "example-api",
    "apiDesc": "example-api-desc",
    "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
    "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
    "insNum": 0,
    "initInstanceNum": 0,
    "status": 1,
    "updateTime": "",
    "isPublic": 1,
    "nameAndReturn": "",
    "apiId": 0
  },
  "apiId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|api|Api|body|true|Api|Api|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|接口名称||false|string||
|&emsp;&emsp;apiDesc|接口描述||false|string||
|&emsp;&emsp;code|接口代码||false|string||
|&emsp;&emsp;url|访问路径||false|string||
|&emsp;&emsp;insNum|||false|integer(int32)||
|&emsp;&emsp;initInstanceNum|||false|integer(int32)||
|&emsp;&emsp;status|接口状态||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;isPublic|是否公开||false|integer(int32)||
|&emsp;&emsp;nameAndReturn|||false|Api|Api|
|&emsp;&emsp;apiId|接口ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 发布


**接口地址**:`/api/svc/publish`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据apiId发布数据服务接口</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "srcId": 1,
  "name": "example-api",
  "apiDesc": "example-api-desc",
  "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
  "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
  "insNum": 0,
  "initInstanceNum": 0,
  "status": 1,
  "updateTime": "",
  "isPublic": 1,
  "nameAndReturn": {
    "groupId": 1,
    "srcId": 1,
    "name": "example-api",
    "apiDesc": "example-api-desc",
    "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
    "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
    "insNum": 0,
    "initInstanceNum": 0,
    "status": 1,
    "updateTime": "",
    "isPublic": 1,
    "nameAndReturn": "",
    "apiId": 0
  },
  "apiId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|api|Api|body|true|Api|Api|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|接口名称||false|string||
|&emsp;&emsp;apiDesc|接口描述||false|string||
|&emsp;&emsp;code|接口代码||false|string||
|&emsp;&emsp;url|访问路径||false|string||
|&emsp;&emsp;insNum|||false|integer(int32)||
|&emsp;&emsp;initInstanceNum|||false|integer(int32)||
|&emsp;&emsp;status|接口状态||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;isPublic|是否公开||false|integer(int32)||
|&emsp;&emsp;nameAndReturn|||false|Api|Api|
|&emsp;&emsp;apiId|接口ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 新建数据接口


**接口地址**:`/api/svc/save`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整具体的数据接口信息，新建数据服务接口</p>



**请求示例**:


```javascript
{
  "apiId": 0,
  "groupId": 1,
  "srcId": 1,
  "name": "example-api",
  "apiDesc": "example-api-desc",
  "code": "select * from user\n<trim prefix=\"where\" prefixOverrides=\"and\">\n    <if test=\"'username' in params\" >\n        username = #{username}\n    </if>\n</trim> ",
  "url": "http://60.245.208.50:9001/svc/example-group-name/example-api-name",
  "isPublic": 1,
  "params": [
    {
      "name": "username",
      "type": "string"
    }
  ]
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiAndParam|ApiAndParam|body|true|ApiAndParam|ApiAndParam|
|&emsp;&emsp;apiId|接口ID||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|接口名称||false|string||
|&emsp;&emsp;apiDesc|接口描述||false|string||
|&emsp;&emsp;code|接口代码||false|string||
|&emsp;&emsp;url|访问路径||false|string||
|&emsp;&emsp;isPublic|是否公开||false|integer(int32)||
|&emsp;&emsp;params|参数列表||false|array|Parameter|
|&emsp;&emsp;&emsp;&emsp;apiId|数据接口ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;name|参数名称||false|string||
|&emsp;&emsp;&emsp;&emsp;type|参数类型||false|string||
|&emsp;&emsp;&emsp;&emsp;defaultValue|参数默认值||false|string||
|&emsp;&emsp;&emsp;&emsp;title|参数描述||false|string||
|&emsp;&emsp;&emsp;&emsp;paramId|接口参数ID||false|integer||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|successful operation|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 接口列表


**接口地址**:`/api/svc/getApiList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取完整的接口列表信息</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 分组接口列表


**接口地址**:`/api/svc/getApiListByGroupId`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取一个数据服务分组的接口列表信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务分组ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 根据接口ID获取接口信息


**接口地址**:`/api/svc/getApiInfoByApiId`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>操作的详细描述</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiId|接口ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|successful operation|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 关键字接口列表


**接口地址**:`/api/svc/filterByKeyword`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json,*/*`


**接口描述**:<p>根据关键字获取匹配的接口列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keyword|关键字|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|400|错误|Response|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


## 接口代码


**接口地址**:`/api/svc/code`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务接口ID获取接口代码</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiId|数据服务接口ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除数据接口


**接口地址**:`/api/svc/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据接口ID删除数据接口</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiId|数据服务接口ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


# 数据服务-分组


## 更新RateLimit插件状态


**接口地址**:`/api/svc/group/updateRateLimitStatus`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据关键的RateLimit信息，更新插件状态</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "rateLimit": 5,
  "timeWindow": 60,
  "status": 0,
  "updateTime": "",
  "rateLimitId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicRateLimit|PluginDynamicRateLimit|body|true|PluginDynamicRateLimit|PluginDynamicRateLimit|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;rateLimit|最大访问次数||false|integer(int32)||
|&emsp;&emsp;timeWindow|访问时间窗口||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;rateLimitId|rate-limit插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新RateLimit插件


**接口地址**:`/api/svc/group/updateRateLimitPlugin`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据完整的RateLimit信息，更新插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "rateLimit": 5,
  "timeWindow": 60,
  "status": 0,
  "updateTime": "",
  "rateLimitId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicRateLimit|PluginDynamicRateLimit|body|true|PluginDynamicRateLimit|PluginDynamicRateLimit|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;rateLimit|最大访问次数||false|integer(int32)||
|&emsp;&emsp;timeWindow|访问时间窗口||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;rateLimitId|rate-limit插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新KeyAuth插件状态


**接口地址**:`/api/svc/group/updateKeyAuthStatus`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据插件id更新插件状态</p>



**请求示例**:


```javascript
{
  "srcId": 1,
  "name": "example-group",
  "groupDesc": "example-group-desc",
  "createTime": "",
  "keyAuthStatus": 0,
  "aclStatus": 0,
  "rateLimitStatus": 0,
  "dynamicStatus": 0,
  "groupId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|group|Group|body|true|Group|Group|
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|数据服务组名称||false|string||
|&emsp;&emsp;groupDesc|数据服务组描述||false|string||
|&emsp;&emsp;createTime|||false|string(date-time)||
|&emsp;&emsp;keyAuthStatus|数据服务组是否有key-auth插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;aclStatus|数据服务组是否有acl插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;rateLimitStatus|数据服务组是否有rate-limit插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;dynamicStatus|数据服务组是否有dynamic-route插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新KeyAuth插件


**接口地址**:`/api/svc/group/updateKeyAuthPlugin`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整的KeyAuth插件信息，更新插件</p>



**请求示例**:


```javascript
{
  "srcId": 1,
  "name": "example-group",
  "groupDesc": "example-group-desc",
  "createTime": "",
  "keyAuthStatus": 0,
  "aclStatus": 0,
  "rateLimitStatus": 0,
  "dynamicStatus": 0,
  "groupId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|group|Group|body|true|Group|Group|
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|数据服务组名称||false|string||
|&emsp;&emsp;groupDesc|数据服务组描述||false|string||
|&emsp;&emsp;createTime|||false|string(date-time)||
|&emsp;&emsp;keyAuthStatus|数据服务组是否有key-auth插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;aclStatus|数据服务组是否有acl插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;rateLimitStatus|数据服务组是否有rate-limit插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;dynamicStatus|数据服务组是否有dynamic-route插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新DynamicExpansion插件状态


**接口地址**:`/api/svc/group/updateDynamicExpansionStatus`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据关键的DynamicExpansion信息，更新插件状态</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "rateLimit": 5,
  "timeWindow": 60,
  "status": 0,
  "updateTime": "",
  "rateLimitId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicRateLimit|PluginDynamicRateLimit|body|true|PluginDynamicRateLimit|PluginDynamicRateLimit|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;rateLimit|最大访问次数||false|integer(int32)||
|&emsp;&emsp;timeWindow|访问时间窗口||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;rateLimitId|rate-limit插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新DynamicExpansion插件


**接口地址**:`/api/svc/group/updateDynamicExpansionPlugin`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据完整的DynamicExpansion信息，更新插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "minValue": 1,
  "maxValue": 5,
  "status": 0,
  "updateTime": "",
  "dynamicExpansionId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicExpansion|PluginDynamicExpansion|body|true|PluginDynamicExpansion|PluginDynamicExpansion|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;minValue|最小副本数量||false|integer(int32)||
|&emsp;&emsp;maxValue|最大副本数量||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;dynamicExpansionId|动态扩容插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新Acl插件状态


**接口地址**:`/api/svc/group/updateAclStatus`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据关键的acl信息，更新插件状态</p>



**请求示例**:


```javascript
{
  "aclId": 0,
  "whiteList": [
    {
      "userId": 1,
      "value": "wbq"
    }
  ],
  "blackList": [
    {
      "userId": 1,
      "value": "wbq"
    }
  ]
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|aclWithId|AclWithId|body|true|AclWithId|AclWithId|
|&emsp;&emsp;aclId|ACL插件ID||false|integer(int32)||
|&emsp;&emsp;whiteList|||false|array|AclUserIdName|
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;value|用户名||false|string||
|&emsp;&emsp;blackList|||false|array|AclUserIdName|
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;value|用户名||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新Acl插件


**接口地址**:`/api/svc/group/updateAclPlugin`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据完整的acl信息，更新插件</p>



**请求示例**:


```javascript
{
  "aclId": 0,
  "whiteList": [
    {
      "userId": 1,
      "value": "wbq"
    }
  ],
  "blackList": [
    {
      "userId": 1,
      "value": "wbq"
    }
  ]
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|aclWithId|AclWithId|body|true|AclWithId|AclWithId|
|&emsp;&emsp;aclId|ACL插件ID||false|integer(int32)||
|&emsp;&emsp;whiteList|||false|array|AclUserIdName|
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;value|用户名||false|string||
|&emsp;&emsp;blackList|||false|array|AclUserIdName|
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;value|用户名||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 数据服务信息


**接口地址**:`/api/svc/group/group`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务分组的id获取数据服务具体信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 创建数据服务分组


**接口地址**:`/api/svc/group/group`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整具体的数据服务信息，添加数据服务分组</p>



**请求示例**:


```javascript
{
  "srcId": 1,
  "name": "example-group",
  "groupDesc": "example-group-desc",
  "createTime": "",
  "keyAuthStatus": 0,
  "aclStatus": 0,
  "rateLimitStatus": 0,
  "dynamicStatus": 0,
  "groupId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|group|Group|body|true|Group|Group|
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|数据服务组名称||false|string||
|&emsp;&emsp;groupDesc|数据服务组描述||false|string||
|&emsp;&emsp;createTime|||false|string(date-time)||
|&emsp;&emsp;keyAuthStatus|数据服务组是否有key-auth插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;aclStatus|数据服务组是否有acl插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;rateLimitStatus|数据服务组是否有rate-limit插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;dynamicStatus|数据服务组是否有dynamic-route插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 更新数据服务


**接口地址**:`/api/svc/group/group`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整的数据服务信息更新数据服务分组</p>



**请求示例**:


```javascript
{
  "srcId": 1,
  "name": "example-group",
  "groupDesc": "example-group-desc",
  "createTime": "",
  "keyAuthStatus": 0,
  "aclStatus": 0,
  "rateLimitStatus": 0,
  "dynamicStatus": 0,
  "groupId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|group|Group|body|true|Group|Group|
|&emsp;&emsp;srcId|数据源ID||false|integer(int32)||
|&emsp;&emsp;name|数据服务组名称||false|string||
|&emsp;&emsp;groupDesc|数据服务组描述||false|string||
|&emsp;&emsp;createTime|||false|string(date-time)||
|&emsp;&emsp;keyAuthStatus|数据服务组是否有key-auth插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;aclStatus|数据服务组是否有acl插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;rateLimitStatus|数据服务组是否有rate-limit插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;dynamicStatus|数据服务组是否有dynamic-route插件，初始一定为0||false|integer(int32)||
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除数据服务


**接口地址**:`/api/svc/group/group`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务分组的 id 和 名称 移除数据服务分组</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||
|groupName|数据服务名称|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加RateLimit插件


**接口地址**:`/api/svc/group/createRateLimitPlugin`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据完整的RateLimit信息，添加插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "rateLimit": 5,
  "timeWindow": 60,
  "status": 0,
  "updateTime": "",
  "rateLimitId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicRateLimit|PluginDynamicRateLimit|body|true|PluginDynamicRateLimit|PluginDynamicRateLimit|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;rateLimit|最大访问次数||false|integer(int32)||
|&emsp;&emsp;timeWindow|访问时间窗口||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;rateLimitId|rate-limit插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加DynamicExpansion插件


**接口地址**:`/api/svc/group/createDynamicExpansionPlugin`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据关键的DynamicExpansion信息，添加插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "minValue": 1,
  "maxValue": 5,
  "status": 0,
  "updateTime": "",
  "dynamicExpansionId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginDynamicExpansion|PluginDynamicExpansion|body|true|PluginDynamicExpansion|PluginDynamicExpansion|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;minValue|最小副本数量||false|integer(int32)||
|&emsp;&emsp;maxValue|最大副本数量||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;dynamicExpansionId|动态扩容插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加Acl插件


**接口地址**:`/api/svc/group/createAclPlugin`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId添加插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "status": 0,
  "updateTime": "",
  "aclId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginAcl|PluginAcl|body|true|PluginAcl|PluginAcl|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;aclId|ACL插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加KeyAuth插件


**接口地址**:`/api/svc/group/addKeyAuthPlugin`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId添加插件</p>



**请求示例**:


```javascript
{
  "groupId": 1,
  "keyName": "example-key-auth-name",
  "keyPwd": "example-key-auth-pwd",
  "status": 0,
  "updateTime": "",
  "keyAuthId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pluginKeyAuth|PluginKeyAuth|body|true|PluginKeyAuth|PluginKeyAuth|
|&emsp;&emsp;groupId|数据服务组ID||false|integer(int32)||
|&emsp;&emsp;keyName|key-auth插件关键字||false|string||
|&emsp;&emsp;keyPwd|key-auth插件凭据||false|string||
|&emsp;&emsp;status|插件状态，初始一定为0||false|integer(int32)||
|&emsp;&emsp;updateTime|||false|string(date-time)||
|&emsp;&emsp;keyAuthId|key-auth插件ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 未分配插件列表


**接口地址**:`/api/svc/group/unallocatedPluginList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务id，获取当前数据服务未分配的插件列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 插件列表


**接口地址**:`/api/svc/group/pluginList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务id，获取当前数据服务已添加的插件列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 获取keyAuth插件信息


**接口地址**:`/api/svc/group/keyAuth`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId获取keyAuth插件信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## getGroupByName


**接口地址**:`/api/svc/group/groupByName`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupName||query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Group|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|srcId|数据源ID|integer(int32)|integer(int32)|
|name|数据服务组名称|string||
|groupDesc|数据服务组描述|string||
|createTime||string(date-time)|string(date-time)|
|keyAuthStatus|数据服务组是否有key-auth插件，初始一定为0|integer(int32)|integer(int32)|
|aclStatus|数据服务组是否有acl插件，初始一定为0|integer(int32)|integer(int32)|
|rateLimitStatus|数据服务组是否有rate-limit插件，初始一定为0|integer(int32)|integer(int32)|
|dynamicStatus|数据服务组是否有dynamic-route插件，初始一定为0|integer(int32)|integer(int32)|
|groupId|数据服务组ID|integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"srcId": 1,
	"name": "example-group",
	"groupDesc": "example-group-desc",
	"createTime": "",
	"keyAuthStatus": 0,
	"aclStatus": 0,
	"rateLimitStatus": 0,
	"dynamicStatus": 0,
	"groupId": 0
}
```


## 数据服务列表


**接口地址**:`/api/svc/group/getGroupList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取系统中数据服务分组的列表</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 获取ACL插件信息


**接口地址**:`/api/svc/group/acl`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId获取ACL插件信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除RateLimit插件


**接口地址**:`/api/svc/group/deleteRateLimitPlugin`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId删除插件</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除KeyAuth插件


**接口地址**:`/api/svc/group/deleteKeyAuthPlugin`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据服务id，删除keyAuth插件</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除DynamicExpansion插件


**接口地址**:`/api/svc/group/deleteDynamicExpansionPlugin`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId删除插件</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 删除Acl插件


**接口地址**:`/api/svc/group/deleteAclPlugin`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据groupId删除插件</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|groupId|数据服务id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


# 数据源


## 更新数据源信息


**接口地址**:`/api/svc/db/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整的数据源信息，更新数据源</p>



**请求示例**:


```javascript
{
  "name": "",
  "srcType": "",
  "driverClassName": "",
  "host": "",
  "port": "",
  "db": "",
  "username": "",
  "password": "",
  "rmark": "",
  "status": 0,
  "srcId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dataSource|DataSource|body|true|DataSource|DataSource|
|&emsp;&emsp;name|||false|string||
|&emsp;&emsp;srcType|||false|string||
|&emsp;&emsp;driverClassName|||false|string||
|&emsp;&emsp;host|||false|string||
|&emsp;&emsp;port|||false|string||
|&emsp;&emsp;db|||false|string||
|&emsp;&emsp;username|||false|string||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;rmark|||false|string||
|&emsp;&emsp;status|||false|integer(int32)||
|&emsp;&emsp;srcId|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加数据源


**接口地址**:`/api/svc/db/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>给出完整具体的数据源信息，添加数据源</p>



**请求示例**:


```javascript
{
  "name": "",
  "srcType": "",
  "driverClassName": "",
  "host": "",
  "port": "",
  "db": "",
  "username": "",
  "password": "",
  "rmark": "",
  "status": 0,
  "srcId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dataSource|DataSource|body|true|DataSource|DataSource|
|&emsp;&emsp;name|||false|string||
|&emsp;&emsp;srcType|||false|string||
|&emsp;&emsp;driverClassName|||false|string||
|&emsp;&emsp;host|||false|string||
|&emsp;&emsp;port|||false|string||
|&emsp;&emsp;db|||false|string||
|&emsp;&emsp;username|||false|string||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;rmark|||false|string||
|&emsp;&emsp;status|||false|integer(int32)||
|&emsp;&emsp;srcId|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 连接测试mysql数据源


**接口地址**:`/api/svc/db/testConnectionMysql`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>连接测试mysql数据源</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|url|数据源url|query|true|string||
|driverClass|驱动类|query|true|string||
|user|用户|query|true|string||
|pwd|凭据|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 连接测试mongodb数据源


**接口地址**:`/api/svc/db/testConnectionMongoDB`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>连接测试mongodb数据源</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|host|主机|query|true|string||
|port|端口|query|true|string||
|database|数据库|query|true|string||
|user|用户|query|true|string||
|pwd|凭据|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## mysql数据源中的所有表


**接口地址**:`/api/svc/db/tableList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取mysql数据源中的所有表的列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dburl|数据源url|query|true|string||
|username|用户名|query|true|string||
|password|凭证|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 数据源列表


**接口地址**:`/api/svc/db/sourceList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取当前系统中已添加的数据源列表</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## mongodb数据源中的所有字典


**接口地址**:`/api/svc/db/mongoDictList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>获取mongodb数据源中所有字典的列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|host|主机|query|true|string||
|port|端口|query|true|string||
|database|数据库|query|true|string||
|user|用户|query|true|string||
|pwd|凭证|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 适配element-ui组件库的数据源信息


**接口地址**:`/api/svc/db/dsForElementSelect`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>通过src_id获取数据源具体信息，并对获取到的数据源信息进行结构调整，以适配前端简便使用element-ui组件的需求</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|srcId|数据源id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 数据源信息


**接口地址**:`/api/svc/db/datasource`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>通过src_id获取数据源具体信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|srcId|数据源id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 移除数据源


**接口地址**:`/api/svc/db/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据数据源id，移除数据源</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|srcId|数据源id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


# 数据接口参数


## 添加多个参数


**接口地址**:`/api/svc/param/adds`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>与add接口相似。虽然提供了这一接口，但我们仍建议规范使用添加数据服务接口的api。此接口实际上是一个失误后的补救措施</p>



**请求示例**:


```javascript
{
  "apiId": 1,
  "name": "example-param-name",
  "type": "string",
  "defaultValue": "example-param-default-value",
  "title": "example-param-title",
  "paramId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|parameter|Parameter|body|true|Parameter|Parameter|
|&emsp;&emsp;apiId|数据接口ID||false|integer(int32)||
|&emsp;&emsp;name|参数名称||false|string||
|&emsp;&emsp;type|参数类型||false|string||
|&emsp;&emsp;defaultValue|参数默认值||false|string||
|&emsp;&emsp;title|参数描述||false|string||
|&emsp;&emsp;paramId|接口参数ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 添加参数


**接口地址**:`/api/svc/param/add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`application/json`


**接口描述**:<p>根据完整的参数信息添加参数。我们虽然提供了这一接口，但我们仍建议规范使用添加数据服务接口的api。此接口实际上是一个失误后的补救措施</p>



**请求示例**:


```javascript
{
  "apiId": 1,
  "name": "example-param-name",
  "type": "string",
  "defaultValue": "example-param-default-value",
  "title": "example-param-title",
  "paramId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|parameter|Parameter|body|true|Parameter|Parameter|
|&emsp;&emsp;apiId|数据接口ID||false|integer(int32)||
|&emsp;&emsp;name|参数名称||false|string||
|&emsp;&emsp;type|参数类型||false|string||
|&emsp;&emsp;defaultValue|参数默认值||false|string||
|&emsp;&emsp;title|参数描述||false|string||
|&emsp;&emsp;paramId|接口参数ID||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


## 获取数据服务接口的参数列表


**接口地址**:`/api/svc/param/params`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>根据apiId获取参数列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apiId|数据接口id|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```


# 用户模块


## 用户列表


**接口地址**:`/api/svc/user/inputAdviseUserList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json`


**接口描述**:<p>完整的用户列表，主要用于提供黑白名单输入建议</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功|Response|
|default|error|ErrorModel|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|内部状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data|返回实体，类型取决于具体的接口|object||


**响应示例**:
```javascript
{
	"success": true,
	"code": 20000,
	"message": "成功",
	"data": {}
}
```


**响应状态码-default**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|success|是否成功|boolean||
|code|状态码|integer(int32)|integer(int32)|
|message|状态信息|string||
|data||object||


**响应示例**:
```javascript
{
	"success": false,
	"code": 500,
	"message": "error message",
	"data": null
}
```