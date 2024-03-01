from py_mybatis.sql.pdbc_sql_template import *
import pymysql
import os
from app import app  # 导入您的 Flask 应用程序对象
from reloadXML import reloadService # 导入重载xml文件的模块

# group_name should be replaced
group_name = "group1"
if "group_name" in os.environ:
    print("Using custom ini file!")
    group_name = os.environ["group_name"]

loadXML = reloadService()
loadXML.run(group_name=group_name)

#
# if __name__ == '__main__':
#     app.run(debug=True, port="5000", host="0.0.0.0")  # 运行应用程序
