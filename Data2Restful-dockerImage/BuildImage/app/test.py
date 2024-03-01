import ast
import json
import os
import re

import pymongo
import xml.etree.ElementTree as ET
from py_mybatis import MybatisMapperScanner
from pymongo.errors import OperationFailure

from BuildImage.app.service import apiService

if __name__ == '__main__':
    path = os.path.abspath("../mapper/")
    mybatis_mapper_dict = MybatisMapperScanner().mapper_xml_scan(mapper_xml_dir=path)
    # print(any("demo1" + "-select" in py_mapper_instance.list_statement() for py_mapper_instance in mybatis_mapper_dict.mapper_dict.values()))

    # mongo_client = pymongo.MongoClient(host="60.245.209.163", port=27017)
    # database = "user"
    # tree = ET.parse('../mapper/apiMapper.xml')
    # root = tree.getroot()
    # for query_element in root.findall(".//mongo"):
    #     query_id = query_element.get("id")  # 获取查询项的 id 属性
    #     user_input = query_element.text  # 假设查询语句存储在 <query> 元素中
    #     if query_id == 'test-mongo' and user_input:
    #         parts = user_input.split('.', 1)
    #         print(parts)
    #         if len(parts) > 1:
    #             dictionary = parts[0]
    #             command = parts[1] if len(parts) > 1 else ''
    #             query_result = getattr(mongo_client[database][dictionary], command.strip('()'))()
    #             # 处理查询结果，这里只是简单打印
    #             print(f"查询ID: {query_id}")
    #             print("查询语句:", user_input)
    #             print("查询结果:", query_result)
    #         else:
    #             print(f"无效的查询语句(ID: {query_id}): {user_input}")
    #     else:
    #         print(f"查询项缺少查询语句 (ID: {query_id})")
    # mongo_client.close()


def execute_command(collection, command, data):
    # 使用正则表达式提取函数名称和括号内的内容
    match = re.match(r'(\w+)\((.*)\)', command)
    if match:
        function_name = match.group(1)
        result = getattr(collection, function_name)(data)
        return result

    else:
        print("Invalid command format.")
        return None


if __name__ == '__main__':
    print("select:{}".format("select" in "select_one"))
    client = pymongo.MongoClient(
        host="60.245.209.163",
        port=27017,
    )
    database = 'user'
    username = 'your_username'
    password = 'your_password'
    # 判断是否需要进行身份验证
    try:
        # 尝试执行一个无害的操作，例如列出数据库
        client.list_database_names()
    except OperationFailure as e:
        print("Authentication is required")
        # 在这里进行身份验证
        authenticated = client[database].authenticate(username, password)
        if authenticated:
            print("Authentication successful")
        else:
            print("Authentication failed")
    # print(client.get_database(database).get_collection('class').find_one(None))

    user_input = 'class.find_one({"name": "wbq"})'
    parts = user_input.split('.', 1)
    dictionary = parts[0]
    command = parts[1] if len(parts) > 1 else ''
    data = {'name': 'wbq'}
    result = execute_command(client[database][dictionary], command, data)
    print(result)

    # find
    # for post in getattr(client[database][dictionary], 'find()'.strip('()'))():
    #     print(post)

    client.close()

    # res = eval("client.database.user_type.find_one({})")
