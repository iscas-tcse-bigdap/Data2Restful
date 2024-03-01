<div align="center">
<h1 align="center">Data2Restful</h1>


English / [简体中文](./README_CN.md)

</div>

<div align="left">

Welcome to the official repository for the Data2Restful project. Data2Restful is a data service management system designed to integrate multiple services such as data service release, management, maintenance, and testing. The data service publishing platform is designed to simplify the process for users to publish data services, and users do not have to focus on content other than business logic. Our system can help users publish and manage data services quickly and efficiently in a more concise way, thereby improving the user experience. The project is currently in the early stages of development and is not yet ready for production use.

## What is Data2Restful ?

Data2Restful is a data service management system that can simplify the user's database operation process and has the following functions:

- **Unified design window for heterogeneous data sources**: In the Data2Restful system, after adding data sources from different sources, users can write their own operation codes in the same window according to the characteristics of different data sources.
- **Release, management, maintenance and testing of data service interfaces**: The Data2Restful provides a complete process for all data services throughout their entire life cycle. The data service interface is generally a specific operation for adding, deleting, modifying and checking the data source. The Data2Restful system provides a special way to package and publish this data service interface to the k8s cluster, and provides an API in a Restful manner. Other users can call the data services designed by the publisher through this API. In addition, the Data2Restful also provides a testing platform for data services. After publishing the data service, users can more easily test the current data service in the test platform to verify its correctness.
- **Data service function expansion**: The Data2Restful system provides an interesting plug-in mechanism for data service grouping. Users can add plug-ins with extended functionality to data services in an extremely simple way. Currently we have designed four types of plug-ins:
  - key-auth: This plug-in adds keyword verification requirements to interfaces in data services. That is, the interface that accesses the data service needs to carry this keyword and the corresponding credentials in the request header.
  - acl: This plug-in provides access control functions for data services, mainly for filtering black and white lists. Using this feature requires a complete user module, and a simple example is provided in this project.
  - rate-limit: This plug-in provides access frequency control for data services. When the access rate control plug-in is enabled, the Data2Restful will limit the access frequency to the current data service at the gateway level to improve the security of the data service.
  - dynamic-expansion: This plug-in provides horizontal dynamic expansion capabilities for data services. When the dynamic expansion plug-in is enabled, the data service will dynamically adjust the number of nodes based on the resource occupancy of existing nodes to ensure the high availability of the current data service.



## How to use our project ?

[Demo](http://60.245.208.50:8089/#/api) / [User Manual](./doc/user_maunal.md) / [Interface documentation](./doc/api_doc.md) / [Online documentation](http://60.245.208.50:8081/doc.html#/home) 



## Tutorial

Data source, data service group and data service interface

- [Configure the Mysql data source and create the first data service and interface](./doc/example/mysql.md)
- [Configure the Mongo data source and create the first data service and interface](./doc/example/mongo.md)

Data service group plug-in

- [Plug-in mechanism](./doc/example/plugin.md)

For more specific usage requirements, please refer to [Interface documentation](./doc/api_doc.md)

