## 配置mongoDB数据源并创建第一个数据服务组和接口的教程

1. 首先，你需要配置一个自己的数据源，使用API`POST localhost:9001/api/svc/db/create` 。本系统要求在请求体中提供数据类型srcType、驱动类driverClassName（取决于数据库类型，不一定需要，对于mongoDB来说是不需要的）、主机host、端口port、指定数据库db、用户名username、密码password。另外，你可以自定义数据源的名称和备注，前者是必须的。我们给出一个简单的示例：

> 我们强烈建议在设置状态为1之前，先使用数据源连接测试接口测试连接，以保证数据源的可用性。

```markdown
POST
localhost:9001/api/svc/db/create
request_body: {
	"name": demo_src,
    "srcType": "mongodb",
    "host": "localhost",
    "port": "27017",
    "db": "database-test",
    "username": "user",
    "password": "pwd",
    "rmark": "this is a optional describe",
    "status": 1  // We recommend that after the test connection is successful, set the status to 1 to indicate that the data source is available.
}
```

2. 第二步，你需要创建一个数据服务分组来管理一系列的数据服务（当然也可以使用现有的数据服务，如果有的话），使用API `POST host:9001/api/svc/group/group` 。我们简化了数据服务的创建流程，因此你只需要在请求体中提供提供一个数据服务名称、可选的数据服务描述和该数据服务组所使用的数据源。系统会在后台自动化完成所有具体的配置。一个简单的示例是：

```markdown
POST
localhost:9001/api/svc/group/group
request_body: {
	"name": "group-test",
    "groupDesc": "this is a optional describe",
    "srcId": 1  // the src_id you get in step 1.
}
```

3. 现在，你可以基于现有的数据源和数据服务，自定义一个数据服务接口，使用API `POST host:9001/api/svc/publish/save` 。例如你现在有一个用户表，并且你想要提供一个接口用来根据关键字name获取class字典中用户信息。一个简单的示例是：

```markdown
POST 
host:9001/api/svc/publish/save
request_body: {
	"groupId": 1,   // the group_id you get in step 2.
    "srcId": 1,     // the src_id you get in step 1.
    "name": "find-with-params",
    "apiDesc": "This is an interface for testing the 'find' command with parameters.",
    "code": "class.find({
            name = #{name}
            })",
    "url": "http://localhost:9001/svc/group-test/find-with-params",  // This URL is the request path provided to external access after the interface is published.
    "params": [
    	{
    		"name": "username",
    		"type": "String"
    		"titie": "this is a optional describe for param"
    	}
    ],
    "isPublic": 1,   // 1 indicates a public interface, accessible to everyone. 0 indicates the authorization interface, which requires user authentication.
}
```

4. 最后，你可以使用API `PUT host:9001/api/svc/publish` 发布你的数据服务接口。然后就可以调用你的数据访问接口了。

```markdown
PUT 
host:9001/api/svc/publish
request_body: {
	'apiId': 1,    // the api_id you get in step 3.
    'srcId': 1,    // the src_id you get in step 1.
    'groupId': 1   // the group_id you get in step 2.
}
```

