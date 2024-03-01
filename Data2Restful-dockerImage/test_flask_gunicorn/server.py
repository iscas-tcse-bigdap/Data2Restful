import sys
from gunicorn.app.wsgiapp import run



def main():
    # Initialize Gunicorn app
    sys.argv = ["gunicorn", "app:app",
                "--bind", "0.0.0.0:8000",  # 绑定的地址和端口
                "--workers", "2",         # 工作进程数
                "--threads", "2",         # 每个工作进程的线程数
               ]
    run()

if __name__ == '__main__':
    # app.run(debug=True)  # 运行应用程序
    main()
