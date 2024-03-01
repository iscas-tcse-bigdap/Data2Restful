import logging

from py_mybatis.sql.mybatis_sql_session import MybatisMapperScanner, MybatisSqlSession, PooledDB
import pymysql
import os


class apiService:

    def __init__(self):
        print("test")

        host = os.environ.get('MYSQL_HOST')
        port = int(os.environ.get('MYSQL_PORT'))

        user = os.environ.get('MYSQL_USER')
        pwd = os.environ.get('MYSQL_ROOT_PASSWORD')
        database = os.environ.get('MYSQL_DATABASE')
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
            charset='utf8'
        )
        self.sql_session = MybatisSqlSession(mapper_dict=mybatis_mapper_dict, dataSource=pool)
        self.sql_namespace = 'apiMapper.'

    def run(self, sqlid, params):
        print("--------------")
        print(params)
        self.sql_id = sqlid
        self.sql_id = self.sql_namespace + self.sql_id
        result = self.sql_session.select_list(self.sql_id, params=params)
        logging.info("in run() of apiService.py, result:{}".format(self.sql_id, result))
        print("============{}============,sql_result:{}".format(self.sql_id, result))
        return result
