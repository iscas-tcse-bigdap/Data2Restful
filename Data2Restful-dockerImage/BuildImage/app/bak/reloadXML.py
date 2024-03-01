import pymysql
import pandas as pd
import xml.dom.minidom as minidom
import os


class RawText(minidom.Text):
    """
    patching minidom.Text.writexml:1087
    the original calls minidom._write_data:302
    below is a combined version of both, but without the '&' replacements and so on.

    author: wbq
    description: minidom自带的xml写入函数会自动将特殊字符转义，而我们并不需要，因此对该方法进行重写
    reference: https://www.jianshu.com/p/b4189c7d215c
    """

    def writexml(self, writer, indent="", addindent="", newl=""):
        if self.data:
            writer.write("{}{}{}".format(indent, self.data, newl))


class reloadService:
    def __init__(self):
        try:
            self.conn = pymysql.connect(
                user="root",
                password="Oncedi@2020",
                host="60.245.209.102",
                database="datacenter",
                port=13306,
                charset="utf8mb4",
            )
            print("数据库成功连接")
        except pymysql.Error as e:
            print("数据库连接失败")

    def create_element(self, element_type, published_api, dom):
        new_element = dom.createElement(element_type)
        new_element.setAttribute("id", published_api[0])
        new_element.setAttribute("resultType", "map")
        textNode = RawText()
        textNode.data = published_api[1]
        new_element.appendChild(textNode)

        root = dom.documentElement
        root.appendChild(new_element)

    def run(self, group_name):
        print(group_name)
        query_sql = "select api.name, api.code from api left join apigroup on api.group_id = apigroup.group_id where apigroup.name = %s"

        df = pd.read_sql(query_sql, self.conn, params=(group_name,))
        published_api_list = df.values

        # Using a relative path in the minidom.parse method will cause the problem that the file cannot be found.
        # We choose using the os module to obtain the absolute path.
        current_dir = os.path.dirname(os.path.abspath(__file__))
        xml_file = os.path.join(current_dir, "../mapper/apiMapper.xml")
        dom = minidom.parse(xml_file)
        for published_api in published_api_list:
            published_api_type = published_api[1].split()[0].lower()
            if published_api_type == "select":
                self.create_element("select", published_api, dom)
            elif published_api_type == "update":
                self.create_element("update", published_api, dom)
            elif published_api_type == "insert":
                self.create_element("insert", published_api, dom)
            elif published_api_type == "delete":
                self.create_element("delete", published_api, dom)
        with open(xml_file, "w", encoding="UTF-8") as f:
            dom.writexml(f, addindent="\t", newl="\n", encoding="UTF-8")
