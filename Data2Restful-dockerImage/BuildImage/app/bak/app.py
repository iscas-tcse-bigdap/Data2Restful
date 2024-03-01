import logging

from flask import Flask, request
from service import apiService

# 创建 Flask 应用程序对象
app = Flask(__name__)


# 定义一个简单的路由
@app.route('/')
def hello():
    return 'Hello, Flask!'


@app.route('/<apiName>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def run(apiName):
    all_params = request.args.to_dict()
    print(all_params)
    # logging.basicConfig(level=logging.INFO)
    logging.info("in run() of app.py, apiName:" + apiName)
    # 创建类的实例并调用类方法
    obj = apiService()
    result = obj.run(sqlid=apiName, params=all_params)
    return result
