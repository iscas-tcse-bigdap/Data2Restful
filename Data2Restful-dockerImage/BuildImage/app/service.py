import json
import logging
import re

from bson import json_util
from flask import jsonify
from py_mybatis.sql.mybatis_sql_session import (
    MybatisMapperScanner,
    MybatisSqlSession,
    PooledDB,
)
import pymysql
import os
import pymongo
import xml.etree.ElementTree as ET

from pymongo.errors import OperationFailure


class apiService:
    def __init__(self):
        print("test")

        host = os.environ.get("HOST")
        port = int(os.environ.get("PORT"))

        user = os.environ.get("USER")
        pwd = os.environ.get("PASSWORD")
        database = os.environ.get("DATABASE")
        print(host)
        print(port)
        print(user)
        print(database)
        path = os.path.abspath("../mapper/")
        mapper_scanner = MybatisMapperScanner()
        mybatis_mapper_dict = mapper_scanner.mapper_xml_scan(mapper_xml_dir=path)
        pool = PooledDB(
            creator=pymysql,
            maxconnections=6,
            mincached=2,
            maxcached=5,
            blocking=True,
            maxusage=None,
            setsession=[],
            ping=0,
            host=host,
            port=port,
            user=user,
            password=pwd,
            database=database,
            cursorclass=pymysql.cursors.DictCursor,
            charset="utf8",
        )
        self.sql_session = MybatisSqlSession(
            mapper_dict=mybatis_mapper_dict, dataSource=pool
        )
        self.sql_namespace = "apiMapper."

    def check_sql_id_exists(self, sql_id):
        path = os.path.abspath("../mapper/")
        mybatis_mapper_dict = MybatisMapperScanner().mapper_xml_scan(mapper_xml_dir=path)
        return any(sql_id in py_mapper_instance.list_statement() for py_mapper_instance in
                   mybatis_mapper_dict.mapper_dict.values())

    def get(self, sqlid, params):
        print(params)
        self.sql_id = sqlid
        self.sql_id = self.sql_namespace + self.sql_id + "-select"
        if not self.check_sql_id_exists(sqlid + "-select"):
            return {"status": "error", "message": "This interface does not currently support this method"}
        result = self.sql_session.select_list(self.sql_id, params=params)
        logging.info("in run() of apiService.py, result:{}".format(self.sql_id, result))
        print("============{}============,sql_result:{}".format(self.sql_id, result))
        return result

    def post(self, sqlid, data):
        self.sql_id = sqlid
        self.sql_id = self.sql_namespace + self.sql_id + "-insert"
        logging.debug("check sqlid:{}".format(sqlid + "-insert"))
        try:
            result = self.sql_session.insert(self.sql_id, params=data)
            logging.info(
                "in run() of apiService.py, result:{}".format(self.sql_id, result)
            )
            if result:
                return {"status": "success", "message": "Insert successful."}
            else:
                return {"status": "error", "message": "Insert failed."}
        except pymysql.err.IntegrityError as e:
            # 处理唯一键冲突异常
            error_code, error_message = e.args
            if error_code == 1062 and "unique_username" in error_message:
                return {
                    "status": "error",
                    "message": "Username already exists. Please choose a different username.",
                }
            else:
                # 其他唯一键冲突情况的处理
                return {
                    "status": "error",
                    "message": "Insert failed due to a unique constraint violation.",
                }
        except Exception as e:
            return {"status": "error", "message": f"An error occurred: {str(e)}"}

    def put(self, sqlid, data):
        self.sql_id = sqlid
        self.sql_id = self.sql_namespace + self.sql_id + "-update"
        try:
            result = self.sql_session.update(self.sql_id, params=data)
            logging.info(
                "in run() of apiService.py, result:{}".format(self.sql_id, result)
            )
            if result:
                return {"status": "success", "message": "Update successful."}
            else:
                return {"status": "error", "message": "Update failed."}
        except Exception as e:
            return {"status": "error", "message": f"An error occurred: {str(e)}"}

    def delete(self, sqlid, data):
        self.sql_id = sqlid
        self.sql_id = self.sql_namespace + self.sql_id + "-delete"
        try:
            result = self.sql_session.delete(self.sql_id, params=data)
            logging.info(
                "in run() of apiService.py, result:{}".format(self.sql_id, result)
            )
            if result:
                return {"status": "success", "message": "Delete successful."}
            else:
                return {"status": "error", "message": "Delete failed."}
        except Exception as e:
            return {"status": "error", "message": f"An error occurred: {str(e)}"}


def execute_command(collect, command, data):
    match = re.match(r'(\w+)\((.*)\)', command, re.DOTALL)
    logging.info("in execute_command() of apiService.py, command:{}, match:{}".format(command, match))
    if match:
        function_name = match.group(1)
        logging.info("in execute_command() of apiService.py, function_name:{}".format(function_name))
        try:
            result = getattr(collect, function_name)(data)
            if "insert" in function_name:
                return jsonify({"message": "Data inserted successfully"})
            elif "update" in function_name:
                return jsonify({"message": "Data updated successfully"})
            elif "delete" in function_name:
                return jsonify({"message": "Data deleted successfully"})
            else:
                result_json = json_util.dumps(result)
                return result_json
        except Exception as e:
            logging.error("Error executing command: {}".format(e))
            return {"error": str(e)}
    else:
        logging.info("Invalid command format.")
        return None


class mongoService:
    def __init__(self):
        host = os.environ.get("HOST")
        port = int(os.environ.get("PORT"))
        user = os.environ.get("USER")
        pwd = os.environ.get("PASSWORD")
        database = os.environ.get("DATABASE")
        self.conn = pymongo.MongoClient(
            host=host,
            port=port,
        )
        # 判断是否需要进行身份验证
        try:
            # 尝试执行一个无害的操作，例如列出数据库
            self.conn.list_database_names()
        except OperationFailure as e:
            print("Authentication is required")
            self.conn[database].authenticate(user, pwd)
        self.db = self.conn[database]
        xml_file = os.path.abspath("../mapper/apiMapper.xml")
        tree = ET.parse(xml_file)
        self.root = tree.getroot()

    def mongo(self, sqlid, data):
        for query_element in self.root.findall(".//mongo"):
            if query_element.attrib["id"] == sqlid + "-mongo":
                user_input = query_element.text
                parts = user_input.split('.', 1)
                dictionary = parts[0]
                command = parts[1] if len(parts) > 1 else ''
                result = execute_command(self.db[dictionary], command, data)
                logging.info("in mongo() of service.py, sqlId:{}, result:{}, data:{}".format(sqlid, result, data))

                return result
