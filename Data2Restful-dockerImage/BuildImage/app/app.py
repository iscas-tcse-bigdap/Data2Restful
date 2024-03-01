import logging
import os

from flask import Flask, request
from service import apiService, mongoService

# 创建 Flask 应用程序对象
app = Flask(__name__)


# 定义一个简单的路由
@app.route("/")
def hello():
    return "Hello, Flask!"


@app.route("/<apiName>", methods=["GET", "POST", "PUT", "DELETE"])
def run(apiName):
    # logging.basicConfig(level=logging.INFO)
    logging.info("in run() of app.py, apiName:" + apiName)

    if os.environ.get("SRCTYPE") == "mysql":
        # 创建类的实例并调用类方法
        obj = apiService()
        if request.method == "GET":
            all_params = request.args.to_dict()
            result = obj.get(sqlid=apiName, params=all_params)
            return result
        elif request.method == "POST":
            data = request.form if request.form else request.json
            result = obj.post(sqlid=apiName, data=data)
            return result
        elif request.method == "PUT":
            data = request.form if request.form else request.json
            result = obj.put(sqlid=apiName, data=data)
            return result
        elif request.method == "DELETE":
            all_params = request.args.to_dict()
            result = obj.delete(sqlid=apiName, data=all_params)
            return result
    elif os.environ.get("SRCTYPE") == "mongodb":
        obj = mongoService()
        # if request.method == "GET":
        #     all_params = request.args.to_dict()
        #     result = obj.mongo(sqlid=apiName, data=all_params)
        #     return result
        # elif request.method == "POST":
        #     data = request.form if request.form else request.json
        #     result = obj.mongo(sqlid=apiName, data=data)
        #     return result
        # else:
        #     return {"status": "error", "message": "This interface does not currently support this method"}
        all_params = request.args.to_dict()
        result = obj.mongo(sqlid=apiName, data=all_params)
        return result
