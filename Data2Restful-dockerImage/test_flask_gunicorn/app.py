import sys
from flask import Flask, request
import time
  
# 创建 Flask 应用程序对象
app = Flask(__name__)


# 定义一个简单的路由
@app.route('/')
def hello():
    time.sleep(2)
    return 'Hello, Flask!'



if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=8000)  # 运行应用程序
