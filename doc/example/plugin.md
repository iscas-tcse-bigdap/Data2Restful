## 插件机制

### KeyAuth插件

1. 对于一个新的未被分配插件的分组，我们可以使用API`POST localhost:9001/api/svc/group/addKeyAuthPlugin` 为数据服务组添加一个KeyAuth插件。系统提供了一对默认的关键字和凭证 `(key_auth, 123)`。

```markdown
POST 
localhost:9001/api/svc/group/addKeyAuthPlugin
request_body: {
	"groupId": 1,   // the data service group id
}
```

2. （可选）用户可以根据需求自定义关键字和凭证。使用API`PUT localhost:9001/api/svc/group/updateKeyAuthPlugin` 修改KeyAuth插件配置。

```markdown
PUT  
localhost:9001/api/svc/group/updateKeyAuthPlugin
request_body: {
	"groupId": 1,   // the data service group id
	"key_name": "key_auth",
	"key_pwd": "123"
}
```

3. 对已添加的KeyAuth插件，我们可以使用API`PUT localhost:9001/api/svc/group/updateKeyAuthStatus` 来启用/禁用该插件。

```markdown
PUT 
localhost:9001/api/svc/group/updateKeyAuthStatus
request_body: {
	"keyAuthId": 1,    // the key_auth plugin id you get from step 1.
    "status": 1        // target status
}
```

### ACL插件

1. 对于一个新的未被分配插件的分组，我们可以使用API`POST localhost:9001/api/svc/group/createAclPlugin` 为数据服务组添加一个acl访问控制插件。

```markdown
POST 
localhost:9001/api/svc/group/createAclPlugin
request_body: {
	"groupId": 1,   // the data service group id
}
```

2. （可选）用户可以根据需求自定义访问控制的黑白名单。使用API`PUT localhost:9001/api/svc/group/updateAclPlugin` 修改acl插件配置。

```markdown
PUT  
localhost:9001/api/svc/group/updateAclPlugin
request_body: {
	'aclId': 1,                        // the acl id you get from step 1.
	"whiteList": [
		{
			"aclUserId": 1,
			"aclId": 1
		},
		{
			"aclUserId": 2,
			"aclId": 1
		}
	],
	"blackList": [
		{
			"aclUserId": 3,
			"aclId": 1
		}
	]
}
```

3. 对已添加的acl插件，我们可以使用API`PUT localhost:9001/api/svc/group/updateAclStatus` 来启用/禁用该插件。

```markdown
PUT 
localhost:9001/api/svc/group/updateAclStatus
request_body: {
	"aclId": : 1,    // the acl plugin id you get from step 1.
    "status": 1        // target status
}
```

### RateLimit插件

1. 对于一个新的未被分配插件的分组，我们可以使用API`POST localhost:9001/api/svc/group/createRateLimitPlugin` 为数据服务组添加一个rateLimit访问频率限制插件。

```markdown
POST 
localhost:9001/api/svc/group/createRateLimitPlugin
request_body: {
	"groupId": 1,   // the data service group id
}
```

2. （可选）用户可以根据需求自定义想要限制的最大访问频率。使用API`PUT localhost:9001/api/svc/group/updateRateLimitPlugin` 修改rateLimit插件配置。

```markdown
PUT  
localhost:9001/api/svc/group/updateRateLimitPlugin
request_body: {
	'groupId': 1,      // the data service group id
	'rateLimit': 5,    
    'timeWindow': 60
}
```

3. 对已添加的rate-limit插件，我们可以使用API`PUT localhost:9001/api/svc/group/updateRateLimitStatus` 来启用/禁用该插件。

```markdown
PUT 
localhost:9001/api/svc/group/updateRateLimitStatus
request_body: {
	"rateLimitId": : 1,    // the plugin id you get from step 1.
    "status": 1        // target status
}
```

### DynamicExpansion插件

1. 对于一个新的未被分配插件的分组，我们可以使用API`POST localhost:9001/api/svc/group/createDynamicExpansionPlugin` 为数据服务组添加一个DynamicExpansion动态水平扩展插件。

```markdown
POST 
localhost:9001/api/svc/group/createDynamicExpansionPlugin
request_body: {
	"groupId": 1,   // the data service group id
}
```

2. （可选）用户可以根据需求自定义集群中数据服务组的副本数量范围，以提高数据服务的可用性和并发性。使用API`PUT localhost:9001/api/svc/group/updateDynamicExpansionPlugin` 修改rateLimit插件配置。

```markdown
PUT  
localhost:9001/api/svc/group/updateDynamicExpansionPlugin
request_body: {
	'groupId': 1,      // the data service group id
	'minValue': 1,
    'maxValue': 3
}
```

3. 对已添加的rate-limit插件，我们可以使用API`PUT localhost:9001/api/svc/group/updateDynamicExpansionStatus` 来启用/禁用该插件。

```markdown
PUT 
localhost:9001/api/svc/group/updateDynamicExpansionStatus
request_body: {
	"groupId": 1,                // the data service group id
    'dynamicExpansionId': 1,     // the plugin id you get from step 1.
    "status": 1                  // target status
}
```

